package com.naas.admin_service.features.users.service.impl;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.core.excel.dto.request.ExportExcelRequest;
import com.naas.admin_service.core.excel.dto.request.SearchFilterRequest;
import com.naas.admin_service.core.excel.dto.response.PageResponse;
import com.naas.admin_service.core.excel.service.ExcelService;
import com.naas.admin_service.core.provider.IdentityStoreService;
import com.naas.admin_service.features.common.tenant.TenantUsernameResolver;
import com.naas.admin_service.features.users.dto.ExportExcelUserDto;
import com.naas.admin_service.features.users.dto.InfUserDto;
import com.naas.admin_service.features.users.dto.RoleResponseDto;
import com.naas.admin_service.features.users.dto.UserDto;
import com.naas.admin_service.features.users.mapper.ExportExcelUserMapper;
import com.naas.admin_service.features.users.service.UserService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.persistence.config.MultitenancyProperties;
import com.ngvgroup.bpm.core.persistence.config.OrganizationContext;
import com.ngvgroup.bpm.core.persistence.config.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.OrganizationRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final TenantUsernameResolver usernameResolver;
    private final IdentityStoreService identityStoreService;
    private final ExcelService excelService;
    private final ExportExcelUserMapper exportExcelUserMapper;

    // ✅ MultitenancyProperties có thể KHÔNG tồn tại khi enabled=false -> dùng
    // ObjectProvider để không crash startup
    private final ObjectProvider<MultitenancyProperties> mtPropsProvider;

    // ✅ Fallback flags (luôn đọc được dù bean MultitenancyProperties không tồn tại)
    @Value("${multitenancy.enabled:false}")
    private boolean multitenancyEnabledFlag;

    @Value("${multitenancy.organizations.enabled:false}")
    private boolean organizationsEnabledFlag;

    // Constants (Sonar-friendly)
    private static final int KC_PAGE_SIZE = 200;

    // READ APIs

    /**
     * LIST:
     * - enabled=false: trả all users (như cũ), không suffix/tenant/org
     * - enabled=true : query theo Organization members, và strip ".tenant" khi
     * response
     */
    @Override
    public List<UserRepresentation> listUser() {
        List<UserRepresentation> raw = listUsersInCurrentTenantRaw();
        return raw.stream()
                .map(this::stripTenantInUserRep)
                .toList();
    }

    /**
     * PAGE:
     * - enabled=false: search trực tiếp Keycloak, totalElements chuẩn (paged)
     * - enabled=true : query theo org members -> filter + paging in-memory
     */
    @Override
    public PageResponse<UserRepresentation> pageUser(SearchFilterRequest request) {
        int pageIdx = request.getPageable().getPage();
        int size = request.getPageable().getSize();
        int offset = pageIdx * size;

        String filter = buildEffectiveSearchFilter(request.getFilter());
        String f = (filter == null) ? "" : filter.trim().toLowerCase();

        // Single mode: giữ như cũ
        if (isSingleMode()) {
            List<UserRepresentation> page = identityStoreService.searchUsers(filter, offset, size);
            int total = identityStoreService.searchUsers(filter).size();

            List<UserRepresentation> resp = (page == null ? List.<UserRepresentation>of() : page)
                    .stream()
                    .map(this::stripTenantInUserRep)
                    .toList();

            return new PageResponse<>(resp, pageIdx, size, total);
        }

        // Multi mode nhưng org-mode tắt (fallback suffix tenant để không vỡ môi trường)
        if (!isOrgMode()) {
            List<UserRepresentation> tenantPageRaw = searchTenantAwarePage(filter, offset, size);
            int totalElements = searchTenantAwareCount(filter);

            List<UserRepresentation> resp = tenantPageRaw.stream()
                    .map(this::stripTenantInUserRep)
                    .toList();

            return new PageResponse<>(resp, pageIdx, size, totalElements);
        }

        // Multi + org-mode: query theo org members
        String orgId = currentOrgIdRequired();
        List<UserRepresentation> members = listOrgMembersPaged(orgId);

        List<UserRepresentation> filtered = members.stream()
                .filter(u -> f.isEmpty()
                        || containsIgnoreCase(u.getUsername(), f)
                        || containsIgnoreCase(u.getEmail(), f)
                        || containsIgnoreCase(u.getFirstName(), f)
                        || containsIgnoreCase(u.getLastName(), f))
                .toList();

        int total = filtered.size();
        List<UserRepresentation> page = (offset >= total)
                ? List.of()
                : filtered.subList(offset, Math.min(offset + size, total));

        List<UserRepresentation> resp = page.stream()
                .map(this::stripTenantInUserRep)
                .toList();

        return new PageResponse<>(resp, pageIdx, size, total);
    }

    // WRITE APIs

    /**
     * CREATE:
     * - enabled=false: username giữ nguyên
     * - enabled=true : username = name.tenant
     * - org add member chỉ chạy khi org-mode bật
     */
    @Override
    public void createUser(UserDto userDto) {
        userDto.setUsername(effectiveUsername(userDto.getUsername()));

        // conflict check theo scope:
        // - enabled=false: toàn realm
        // - enabled=true : org members
        checkUserExist(listUsersInCurrentTenantRaw(), userDto, false, null);

        UserRepresentation userRepresentation = getUserRepresentation(userDto);

        String createdUserId = identityStoreService.createUser(userRepresentation);

        // org-mode tắt => xong luôn
        if (!isOrgMode())
            return;

        String orgAlias = requireOrgAlias();
        try {
            String orgId = requireOrgId(orgAlias);
            addMemberToOrganization(orgId, createdUserId);
        } catch (RuntimeException ex) {
            safeDeleteUser(createdUserId);
            throw ex;
        }
    }

    /**
     * UPDATE:
     * - enabled=true + org-mode: không cho update user org khác
     */
    @Override
    public void updateUser(String userId, UserDto userDto) {
        UserRepresentation current;
        try {
            current = identityStoreService.getUser(userId).toRepresentation();
        } catch (Exception e) {
            throw new BusinessException(CommonErrorCode.USER_ID_NOT_FOUND, userId);
        }

        ensureUserBelongsToCurrentTenant(current);

        userDto.setUsername(effectiveUsername(userDto.getUsername()));
        checkUserExist(listUsersInCurrentTenantRaw(), userDto, true, userId);

        current.setFirstName(userDto.getFirstName());
        current.setLastName(userDto.getLastName());
        current.setUsername(userDto.getUsername());
        current.setEmail(userDto.getEmail());

        identityStoreService.updateUser(userId, current);
    }

    @Override
    public String getUserId(String username) {
        String effective = effectiveUsername(username);

        List<UserRepresentation> users = identityStoreService.searchUsers(effective);
        return users.stream()
                .filter(u -> u.getUsername() != null && u.getUsername().equalsIgnoreCase(effective))
                .findFirst()
                .map(UserRepresentation::getId)
                .orElse(null);
    }

    @Override
    public void changePass(String userName, String newPass) {
        if (newPass == null || newPass.isBlank()) {
            throw new BusinessException(CommonErrorCode.INVALID_NEW_PASSWORD);
        }

        String userId = getUserId(userName);
        if (userId == null) {
            throw new BusinessException(CommonErrorCode.USER_NOT_FOUND);
        }

        try {
            UserRepresentation rep = identityStoreService.getUser(userId).toRepresentation();
            ensureUserBelongsToCurrentTenant(rep);
        } catch (Exception e) {
            throw new BusinessException(CommonErrorCode.USER_NOT_FOUND);
        }

        CredentialRepresentation cr = new CredentialRepresentation();
        cr.setType(CredentialRepresentation.PASSWORD);
        cr.setTemporary(false);
        cr.setValue(newPass);

        identityStoreService.resetUserPassword(userId, cr);
    }

    @Override
    public void updateBranchCode(String username, String branchCode) {
        String effective = effectiveUsername(username);

        List<UserRepresentation> candidates = identityStoreService.searchUsers(effective);

        UserRepresentation target = candidates.stream()
                .filter(u -> u.getUsername() != null && u.getUsername().equalsIgnoreCase(effective))
                .findFirst()
                .orElse(null);

        if (target == null)
            return;

        ensureUserBelongsToCurrentTenant(target);

        UserRepresentation user = identityStoreService.getUser(target.getId()).toRepresentation();

        Map<String, List<String>> attributes = user.getAttributes();
        if (attributes == null)
            attributes = new HashMap<>();

        if (branchCode == null || branchCode.isBlank()) {
            attributes.remove("branchCode");
        } else {
            attributes.put("branchCode", List.of(branchCode.trim()));
        }

        user.setAttributes(attributes);
        identityStoreService.updateUser(target.getId(), user);
    }

    // DTO APIs

    @Override
    public List<InfUserDto> getUserInfoList(List<String> userIds) {
        return userIds.stream()
                .map(this::getUserInfo)
                .toList();
    }

    private InfUserDto getUserInfo(String userId) {
        InfUserDto dto = new InfUserDto();

        UserRepresentation rep = getUser(userId).toRepresentation();
        ensureUserBelongsToCurrentTenant(rep);

        BeanUtils.copyProperties(rep, dto);
        dto.setUsername(stripTenantFromUsername(rep.getUsername()));
        return dto;
    }

    @Override
    public List<InfUserDto> getUsernameList(List<String> listUserName) {
        Set<String> effectiveSet = (listUserName == null ? List.<String>of() : listUserName).stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(this::effectiveUsername)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        List<UserRepresentation> users = listUsersInCurrentTenantRaw();

        return users.stream()
                .filter(u -> u.getUsername() != null && effectiveSet.contains(u.getUsername().toLowerCase()))
                .map(u -> {
                    InfUserDto dto = new InfUserDto();
                    BeanUtils.copyProperties(u, dto);
                    dto.setUsername(stripTenantFromUsername(u.getUsername()));
                    return dto;
                })
                .toList();
    }

    // EXCEL export

    @Override
    public ResponseEntity<byte[]> exportExcel(String fileName, ExportExcelRequest request) {
        List<UserRepresentation> res = listUser();

        List<ExportExcelUserDto> dtos = res.stream()
                .map(u -> {
                    ExportExcelUserDto e = exportExcelUserMapper.toDto(u);
                    e.setUsername(u.getUsername()); // listUser() đã strip
                    String fn = u.getFirstName() == null ? "" : u.getFirstName();
                    String ln = u.getLastName() == null ? "" : u.getLastName();
                    e.setFullName((fn + " " + ln).trim());
                    return e;
                })
                .toList();

        byte[] response = excelService.exportToExcelContent(dtos, request.getLabels(), ExportExcelUserDto.class);

        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".xlsx\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(response);
        } catch (Exception e) {
            log.error("Error: Lỗi tạo file Excel {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // OTHER APIs

    @Override
    public void deleteUser(String userId) {
        this.checkUserIdExist(userId);
        identityStoreService.deleteUser(userId);
    }

    @Override
    public UserResource getUser(String userId) {
        return identityStoreService.getUser(userId);
    }

    @Override
    public List<RoleResponseDto> getUserRoles(String userId) {
        this.checkUserIdExist(userId);
        return identityStoreService.getUserRoles(userId);
    }

    @Override
    public List<GroupRepresentation> getUserGroups(String userId) {
        this.checkUserIdExist(userId);
        return identityStoreService.getUserGroups(userId);
    }

    @Override
    public void assignRole(String userId, List<String> roleNames) {
        this.checkUserIdExist(userId);
        this.checkRoleNamesExist(roleNames);
        identityStoreService.assignRoleToUser(userId, roleNames);
    }

    @Override
    public void unAssignRole(String userId, List<String> roleNames) {
        this.checkUserIdExist(userId);
        this.checkRoleNamesExist(roleNames);
        identityStoreService.unAssignRoleFromUser(userId, roleNames);
    }

    @Override
    public void joinGroups(String userId, List<String> groupIds) {
        this.checkUserIdExist(userId);
        this.checkGroupIdsExist(groupIds);
        identityStoreService.joinGroups(userId, groupIds);
    }

    @Override
    public void leaveGroups(String userId, List<String> groupIds) {
        this.checkUserIdExist(userId);
        this.checkGroupIdsExist(groupIds);
        identityStoreService.leaveGroups(userId, groupIds);
    }

    // Multitenancy mode helpers

    private boolean isSingleEnabled() {
        MultitenancyProperties p = mtPropsProvider.getIfAvailable();
        return (p != null) ? !p.isEnabled() : !multitenancyEnabledFlag;
    }

    private boolean isSingleMode() {
        return isSingleEnabled();
    }

    private boolean isOrgMode() {
        if (isSingleEnabled())
            return false;

        MultitenancyProperties p = mtPropsProvider.getIfAvailable();
        if (p != null) {
            return p.getOrganizations() != null && p.getOrganizations().isEnabled();
        }
        return organizationsEnabledFlag;
    }

    private String currentTenantIdRequired() {
        String t = TenantContext.getTenantId();
        if (t == null || t.isBlank()) {
            throw new BusinessException(CommonErrorCode.KEYCLOAK_UNKNOWN_ERROR);
        }
        return t.trim();
    }

    /**
     * FE truyền "name"
     * - enabled=false: giữ nguyên
     * - enabled=true : name.tenant (chống double append)
     */
    private String effectiveUsername(String rawUsername) {
        if (rawUsername == null)
            return null;
        if (isSingleMode())
            return rawUsername.trim();
        String tenantId = currentTenantIdRequired();
        return usernameResolver.effectiveUsername(rawUsername, true, tenantId);
    }

    private String stripTenantFromUsername(String username) {
        if (username == null)
            return null;
        if (isSingleMode())
            return username.trim();
        String tenantId = currentTenantIdRequired();
        return usernameResolver.stripTenantSuffix(username, true, tenantId);
    }

    /**
     * Validate user thuộc scope hiện tại
     * - enabled=false: true
     * - enabled=true + org-mode: check membership bằng REST (1 call)
     */
    private boolean belongsToCurrentTenant(UserRepresentation u) {
        if (isSingleMode())
            return true;
        if (u == null)
            return false;

        if (isOrgMode()) {
            String userId = u.getId();
            if (userId == null || userId.isBlank())
                return false;

            String orgId = currentOrgIdRequired();
            return isMemberOfOrg(orgId, userId);
        }

        // fallback suffix tenant
        if (u.getUsername() == null)
            return false;
        String tenantId = currentTenantIdRequired();
        return u.getUsername().toLowerCase().endsWith(("." + tenantId).toLowerCase());
    }

    private void ensureUserBelongsToCurrentTenant(UserRepresentation u) {
        if (!belongsToCurrentTenant(u)) {
            throw new BusinessException(CommonErrorCode.USER_NOT_IN_TENANT);
        }
    }

    private UserRepresentation stripTenantInUserRep(UserRepresentation u) {
        if (u == null)
            return null;
        u.setUsername(stripTenantFromUsername(u.getUsername()));
        return u;
    }

    /**
     * Nguồn list users theo mode:
     * - enabled=false: all realm (paged)
     * - enabled=true + org-mode: org members (paged)
     */
    private List<UserRepresentation> listUsersInCurrentTenantRaw() {
        if (isSingleMode()) {
            return listAllUsersPaged();
        }

        if (isOrgMode()) {
            String orgId = currentOrgIdRequired();
            return listOrgMembersPaged(orgId);
        }

        // fallback suffix tenant
        List<UserRepresentation> all = listAllUsersPaged();
        return all.stream().filter(this::belongsToCurrentTenant).toList();
    }

    private String buildEffectiveSearchFilter(String rawFilter) {
        if (rawFilter == null)
            return null;
        String f = rawFilter.trim();
        return f.isEmpty() ? "" : f;
    }

    private List<UserRepresentation> listAllUsersPaged() {
        return identityStoreService.listAllUsersPaged();
    }

    private List<UserRepresentation> searchAllUsersPaged(String filter) {
        return identityStoreService.searchAllUsersPaged(filter);
    }

    /**
     * Tenant-aware paging fallback (khi multi nhưng org-mode tắt)
     */
    private List<UserRepresentation> searchTenantAwarePage(String filter, int tenantOffset, int size) {
        if (isSingleMode()) {
            return identityStoreService.searchUsers(filter, tenantOffset, size);
        }

        TenantPageCollector collector = new TenantPageCollector(tenantOffset, size);

        int first = 0;
        boolean hasMore = true;

        while (collector.needsMore() && hasMore) {
            List<UserRepresentation> page = identityStoreService.searchUsers(filter, first, KC_PAGE_SIZE);

            collector.acceptPage(page, this::belongsToCurrentTenant);

            if (page == null || page.isEmpty()) {
                hasMore = false;
            } else {
                first += KC_PAGE_SIZE;
                hasMore = page.size() == KC_PAGE_SIZE;
            }
        }

        return collector.result();
    }

    private int searchTenantAwareCount(String filter) {
        if (isSingleMode())
            return searchAllUsersPaged(filter).size();
        return (int) searchAllUsersPaged(filter).stream()
                .filter(this::belongsToCurrentTenant)
                .count();
    }

    private boolean containsIgnoreCase(String s, String needleLower) {
        return s != null && s.toLowerCase().contains(needleLower);
    }

    // Organization (multi query by org)

    private String requireOrgAlias() {
        String orgAlias = OrganizationContext.getOrgAlias();
        if (orgAlias == null || orgAlias.isBlank()) {
            throw new BusinessException(CommonErrorCode.KEYCLOAK_UNKNOWN_ERROR);
        }
        return orgAlias.trim();
    }

    private String currentOrgIdRequired() {
        // chỉ gọi khi org-mode bật
        String orgAlias = requireOrgAlias();
        String orgId = resolveOrgIdByAlias(orgAlias);
        if (orgId == null || orgId.isBlank()) {
            throw new BusinessException(CommonErrorCode.KEYCLOAK_UNKNOWN_ERROR);
        }
        return orgId;
    }

    private List<UserRepresentation> listOrgMembersPaged(String orgId) {
        return identityStoreService.listOrgMembersPaged(orgId);
    }

    private boolean isMemberOfOrg(String orgId, String userId) {
        return identityStoreService.isMemberOfOrg(orgId, userId);
    }

    // Organization resolve alias -> id

    private String resolveOrgIdByAlias(String orgAlias) {
        String alias = normalizeAlias(orgAlias);
        if (alias == null)
            return null;

        int max = 200;
        for (int first = 0; first <= 10_000; first += max) {
            List<OrganizationRepresentation> page = loadOrganizationPage(first, max);
            if (page.isEmpty())
                return null;

            String found = findOrgIdInPage(page, alias);
            if (found != null)
                return found;

            if (page.size() < max)
                return null;
        }
        return null;
    }

    private List<OrganizationRepresentation> loadOrganizationPage(int first, int max) {
        try {
            List<OrganizationRepresentation> page = identityStoreService.searchOrganizations("", false, first, max);
            return page == null ? List.of() : page;
        } catch (Exception e) {
            log.warn("organizations().search failed, fallback REST. first={}, max={}, err={}",
                    first, max, e.toString());
            return fetchOrganizationsByRest(first, max);
        }
    }

    private String findOrgIdInPage(List<OrganizationRepresentation> page, String alias) {
        for (OrganizationRepresentation o : page) {
            if (o == null)
                continue;
            String a = o.getAlias();
            if (a != null && a.equalsIgnoreCase(alias)) {
                return o.getId();
            }
        }
        return null;
    }

    private List<OrganizationRepresentation> fetchOrganizationsByRest(int first, int max) {
        return identityStoreService.fetchOrganizationsByRest(first, max);
    }

    private String normalizeAlias(String orgAlias) {
        if (orgAlias == null || orgAlias.isBlank())
            return null;
        return orgAlias.trim();
    }

    // Keycloak create + error map

    private String requireOrgId(String orgAlias) {
        String orgId = resolveOrgIdByAlias(orgAlias);
        if (orgId == null || orgId.isBlank()) {
            throw new BusinessException(CommonErrorCode.KEYCLOAK_UNKNOWN_ERROR);
        }
        return orgId;
    }

    private void addMemberToOrganization(String orgId, String userId) {
        identityStoreService.addMemberToOrganization(orgId, userId);
    }

    @NotNull
    private static UserRepresentation getUserRepresentation(UserDto userDto) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);
        userRepresentation.setFirstName(userDto.getFirstName());
        userRepresentation.setLastName(userDto.getLastName());
        userRepresentation.setUsername(userDto.getUsername());
        userRepresentation.setEmail(userDto.getEmail());
        userRepresentation.setEmailVerified(false);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(userDto.getPassword());
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setTemporary(false);
        userRepresentation.setCredentials(List.of(credentialRepresentation));

        return userRepresentation;
    }

    private void safeDeleteUser(String userId) {
        if (userId == null || userId.isBlank())
            return;
        try {
            identityStoreService.deleteUser(userId);
        } catch (Exception ignored) {
            // best-effort rollback
        }
    }

    // CONFLICT checks

    private void checkUserExist(List<UserRepresentation> userList, UserDto userDto, boolean isUpdate,
            String excludeUserId) {
        if (isUpdate) {
            checkUpdateUserExist(userList, userDto, excludeUserId);
        } else {
            checkCreateUserExist(userList, userDto);
        }
    }

    private void checkCreateUserExist(List<UserRepresentation> userList, UserDto userDto) {
        boolean usernameExist = userList.stream()
                .anyMatch(user -> user.getUsername() != null
                        && userDto.getUsername() != null
                        && user.getUsername().equalsIgnoreCase(userDto.getUsername()));

        boolean emailExist = userList.stream()
                .anyMatch(user -> user.getEmail() != null
                        && userDto.getEmail() != null
                        && user.getEmail().equalsIgnoreCase(userDto.getEmail()));

        if (usernameExist)
            throw new BusinessException(CommonErrorCode.USER_USERNAME_EXIST, true);
        if (emailExist)
            throw new BusinessException(CommonErrorCode.USER_EMAIL_EXIST, true);
    }

    private void checkUpdateUserExist(List<UserRepresentation> userList, UserDto userDto, String excludeUserId) {
        boolean usernameExist = userList.stream()
                .filter(u -> excludeUserId == null || !excludeUserId.equals(u.getId()))
                .anyMatch(user -> user.getUsername() != null
                        && userDto.getUsername() != null
                        && user.getUsername().equalsIgnoreCase(userDto.getUsername()));

        boolean emailExist = userList.stream()
                .filter(u -> excludeUserId == null || !excludeUserId.equals(u.getId()))
                .anyMatch(user -> user.getEmail() != null
                        && userDto.getEmail() != null
                        && user.getEmail().equalsIgnoreCase(userDto.getEmail()));

        if (usernameExist)
            throw new BusinessException(CommonErrorCode.USER_USERNAME_EXIST, true);
        if (emailExist)
            throw new BusinessException(CommonErrorCode.USER_EMAIL_EXIST, true);
    }

    private void checkUserIdExist(String userId) {
        try {
            UserRepresentation rep = getUser(userId).toRepresentation();
            ensureUserBelongsToCurrentTenant(rep);
        } catch (Exception e) {
            throw new BusinessException(CommonErrorCode.USER_ID_NOT_FOUND, userId);
        }
    }

    // GROUP/ROLE validation

    private void checkGroupIdsExist(List<String> groupIds) {
        List<GroupRepresentation> groupList = this.listGroups();
        Map<String, GroupRepresentation> groupMap = groupList.stream()
                .collect(Collectors.toMap(GroupRepresentation::getId, group -> group));

        List<String> errors = new ArrayList<>();
        for (String groupId : groupIds) {
            if (!groupMap.containsKey(groupId)) {
                errors.add(groupId);
            }
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(CommonErrorCode.EXISTS,
                    "Group ID không tồn tại: " + String.join(", ", errors));
        }
    }

    private void checkRoleNamesExist(List<String> roleNames) {
        List<String> errors = new ArrayList<>();
        for (String roleName : roleNames) {
            try {
                identityStoreService.getRole(roleName);
            } catch (Exception e) {
                errors.add(roleName);
            }
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(CommonErrorCode.EXISTS,
                    "Role name không tồn tại: " + String.join(", ", errors));
        }
    }

    // Keycloak resources

    public List<GroupRepresentation> listGroups() {
        return identityStoreService.listGroups();
    }

    private static final class TenantPageCollector {
        private final int offset;
        private final int targetSize;

        private final List<UserRepresentation> result;
        private int skipped = 0;
        private boolean done = false;

        private TenantPageCollector(int offset, int targetSize) {
            this.offset = Math.max(0, offset);
            this.targetSize = Math.max(0, targetSize);
            this.result = new ArrayList<>(this.targetSize);
            if (this.targetSize == 0) {
                this.done = true;
            }
        }

        boolean needsMore() {
            return !done;
        }

        List<UserRepresentation> result() {
            return result;
        }

        void acceptPage(List<UserRepresentation> page,
                java.util.function.Predicate<UserRepresentation> belongsPredicate) {
            if (done || page == null || page.isEmpty())
                return;

            for (UserRepresentation u : page) {
                if (done)
                    return;
                done = acceptUser(u, belongsPredicate);
            }
        }

        private boolean acceptUser(UserRepresentation u,
                java.util.function.Predicate<UserRepresentation> belongsPredicate) {
            if (u == null || !belongsPredicate.test(u)) {
                return false;
            }

            if (skipped < offset) {
                skipped++;
                return false;
            }

            result.add(u);
            return result.size() >= targetSize;
        }
    }
}
