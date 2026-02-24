package com.naas.admin_service.features.users.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.core.contants.KeycloakErrorConstants;
import com.naas.admin_service.core.excel.dto.request.ExportExcelRequest;
import com.naas.admin_service.core.excel.dto.request.SearchFilterRequest;
import com.naas.admin_service.core.excel.dto.response.PageResponse;
import com.naas.admin_service.core.excel.service.ExcelService;
import com.naas.admin_service.features.permission.repository.ComCfgPermissionMapRepository;
import com.naas.admin_service.features.permission.service.PermissionService;
import com.naas.admin_service.features.users.dto.ExportExcelUserDto;
import com.naas.admin_service.features.users.dto.InfUserDto;
import com.naas.admin_service.features.users.dto.RoleResponseDto;
import com.naas.admin_service.features.users.dto.UserDto;
import com.naas.admin_service.features.users.mapper.ExportExcelUserMapper;
import com.naas.admin_service.features.users.service.UserService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.config.MultitenancyProperties;
import com.ngvgroup.bpm.core.persistence.config.OrganizationContext;
import com.ngvgroup.bpm.core.persistence.config.TenantContext;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.OrganizationRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${security.keycloak.realm}")
    private String realm;

    @Value("${security.keycloak.base-url}")
    private String keycloakBaseUrl;

    @Value("${bpm.core.security.permission.use-db:false}")
    private boolean isUsingDb;

    private final Keycloak keycloak;
    private final ExcelService excelService;
    private final ExportExcelUserMapper exportExcelUserMapper;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final PermissionService permissionService;
    private final ComCfgPermissionMapRepository permissionMapRepository;

    // ✅ MultitenancyProperties có thể KHÔNG tồn tại khi enabled=false -> dùng ObjectProvider để không crash startup
    private final ObjectProvider<MultitenancyProperties> mtPropsProvider;

    // ✅ Fallback flags (luôn đọc được dù bean MultitenancyProperties không tồn tại)
    @Value("${multitenancy.enabled:false}")
    private boolean multitenancyEnabledFlag;

    @Value("${multitenancy.organizations.enabled:false}")
    private boolean organizationsEnabledFlag;

    @Value("${security.keycloak.admin-realms-path:/admin/realms/}")
    private String adminRealmsPath;

    
    // Constants (Sonar-friendly)
    private static final int KC_PAGE_SIZE = 200;

    private String adminRealmBaseUrl() {
        return keycloakBaseUrl + adminRealmsPath + realm;
    }

    
    // READ APIs
    

    /**
     * LIST:
     * - enabled=false: trả all users (như cũ), không suffix/tenant/org
     * - enabled=true : query theo Organization members, và strip ".tenant" khi response
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
            List<UserRepresentation> page = getUsersResource().search(filter, offset, size);
            int total = searchAllUsersPaged(filter).size();

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

        UsersResource usersResource = getUsersResource();
        RealmResource realmResource = keycloak.realm(realm);

        String createdUserId = createKeycloakUser(usersResource, userRepresentation);

        // org-mode tắt => xong luôn
        if (!isOrgMode()) return;

        String orgAlias = requireOrgAlias();
        try {
            String orgId = requireOrgId(realmResource, orgAlias);
            addMemberToOrganization(realmResource, orgId, createdUserId);
        } catch (RuntimeException ex) {
            safeDeleteUser(realmResource, createdUserId);
            throw ex;
        }
    }

    /**
     * UPDATE:
     * - enabled=true + org-mode: không cho update user org khác
     */
    @Override
    public void updateUser(String userId, UserDto userDto) {
        UsersResource usersResource = getUsersResource();

        UserRepresentation current;
        try {
            current = usersResource.get(userId).toRepresentation();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.NOT_FOUND, userId);
        }

        ensureUserBelongsToCurrentTenant(current);

        userDto.setUsername(effectiveUsername(userDto.getUsername()));
        checkUserExist(listUsersInCurrentTenantRaw(), userDto, true, userId);

        current.setFirstName(userDto.getFirstName());
        current.setLastName(userDto.getLastName());
        current.setUsername(userDto.getUsername());
        current.setEmail(userDto.getEmail());

        usersResource.get(userId).update(current);
    }

    @Override
    public String getUserId(String username) {
        String effective = effectiveUsername(username);

        List<UserRepresentation> users = keycloak.realm(realm).users().search(effective);
        return users.stream()
                .filter(u -> u.getUsername() != null && u.getUsername().equalsIgnoreCase(effective))
                .findFirst()
                .map(UserRepresentation::getId)
                .orElse(null);
    }

    @Override
    public void changePass(String userName, String newPass) {
        String userId = this.getUserId(userName);
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, userName);
        }

        CredentialRepresentation newPassword = new CredentialRepresentation();
        newPassword.setType(CredentialRepresentation.PASSWORD);
        newPassword.setTemporary(false);
        newPassword.setValue(newPass);

        keycloak.realm(realm)
                .users()
                .get(userId)
                .resetPassword(newPassword);
    }

    @Override
    public void updateBranchCode(String username, String branchCode) {
        String effective = effectiveUsername(username);

        List<UserRepresentation> candidates = keycloak.realm(realm).users().search(effective);

        UserRepresentation target = candidates.stream()
                .filter(u -> u.getUsername() != null && u.getUsername().equalsIgnoreCase(effective))
                .findFirst()
                .orElse(null);

        if (target == null) return;

        ensureUserBelongsToCurrentTenant(target);

        UserResource userResource = keycloak.realm(realm).users().get(target.getId());
        UserRepresentation user = userResource.toRepresentation();

        Map<String, List<String>> attributes = user.getAttributes();
        if (attributes == null) attributes = new HashMap<>();

        if (branchCode == null || branchCode.isBlank()) {
            attributes.remove("branchCode");
        } else {
            attributes.put("branchCode", List.of(branchCode.trim()));
        }

        user.setAttributes(attributes);
        userResource.update(user);
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
        getUsersResource().delete(userId);
    }

    @Override
    public UserResource getUser(String userId) {
        return getUsersResource().get(userId);
    }

    @Override
    public List<RoleResponseDto> getUserRoles(String userId) {
        this.checkUserIdExist(userId);
        if (isUsingDb) {
            return permissionMapRepository.findPermissionInfoByUserId(userId);
        } else {
            return getUser(userId).roles().realmLevel().listAll().stream()
                    .map(r -> new RoleResponseDto(r.getName(), r.getDescription()))
                    .toList();
        }
    }

    @Override
    public List<GroupRepresentation> getUserGroups(String userId) {
        this.checkUserIdExist(userId);
        return getUser(userId).groups();
    }

    @Override
    public void assignRole(String userId, List<String> roleNames) {
        this.checkUserIdExist(userId);
        this.checkRoleNamesExist(roleNames);
        if (isUsingDb) {
            permissionService.assignRolesToUser(userId, roleNames);
        } else {
            List<RoleRepresentation> rolesToAssign = roleNames.stream()
                    .map(roleName -> getRolesResource().get(roleName).toRepresentation())
                    .toList();
            getUser(userId).roles().realmLevel().add(rolesToAssign);
        }
    }

    @Override
    public void unAssignRole(String userId, List<String> roleNames) {
        this.checkUserIdExist(userId);
        this.checkRoleNamesExist(roleNames);
        if (isUsingDb) {
            permissionService.unAssignRolesFromUser(userId, roleNames);
        } else {
            List<RoleRepresentation> roles = roleNames.stream()
                    .map(roleName -> getRolesResource().get(roleName).toRepresentation())
                    .toList();
            getUser(userId).roles().realmLevel().remove(roles);
        }
    }

    @Override
    public void joinGroups(String userId, List<String> groupIds) {
        this.checkUserIdExist(userId);
        this.checkGroupIdsExist(groupIds);
        UserResource user = getUser(userId);
        groupIds.forEach(user::joinGroup);
    }

    @Override
    public void leaveGroups(String userId, List<String> groupIds) {
        this.checkUserIdExist(userId);
        this.checkGroupIdsExist(groupIds);
        UserResource user = getUser(userId);
        groupIds.forEach(user::leaveGroup);
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
        if (isSingleEnabled()) return false;

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
        if (rawUsername == null) return null;

        String u = rawUsername.trim();
        if (u.isEmpty()) return u;

        if (isSingleMode()) return u;

        String tenantId = currentTenantIdRequired();
        String suffix = "." + tenantId;

        if (u.toLowerCase().endsWith(suffix.toLowerCase())) return u;
        return u + suffix;
    }

    private String stripTenantFromUsername(String username) {
        if (username == null || username.isBlank()) return username;
        if (isSingleMode()) return username;

        String tenantId = currentTenantIdRequired();
        String suffix = "." + tenantId;

        if (username.toLowerCase().endsWith(suffix.toLowerCase())) {
            return username.substring(0, username.length() - suffix.length());
        }
        return username;
    }

    /**
     * Validate user thuộc scope hiện tại
     * - enabled=false: true
     * - enabled=true + org-mode: check membership bằng REST (1 call)
     */
    private boolean belongsToCurrentTenant(UserRepresentation u) {
        if (isSingleMode()) return true;
        if (u == null) return false;

        if (isOrgMode()) {
            String userId = u.getId();
            if (userId == null || userId.isBlank()) return false;

            String orgId = currentOrgIdRequired();
            return isMemberOfOrg(orgId, userId);
        }

        // fallback suffix tenant
        if (u.getUsername() == null) return false;
        String tenantId = currentTenantIdRequired();
        return u.getUsername().toLowerCase().endsWith(("." + tenantId).toLowerCase());
    }

    private void ensureUserBelongsToCurrentTenant(UserRepresentation u) {
        if (!belongsToCurrentTenant(u)) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "User not in current tenant/org");
        }
    }

    private UserRepresentation stripTenantInUserRep(UserRepresentation u) {
        if (u == null) return null;
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
        if (rawFilter == null) return null;
        String f = rawFilter.trim();
        return f.isEmpty() ? "" : f;
    }

    private List<UserRepresentation> listAllUsersPaged() {
        UsersResource users = getUsersResource();
        List<UserRepresentation> all = new ArrayList<>();

        int first = 0;
        boolean hasMore = true;

        while (hasMore) {
            List<UserRepresentation> page;

            try {
                // KC mới: có list(first, max)
                page = users.list(first, KC_PAGE_SIZE);
            } catch (Exception ex) {
                // KC cũ: không support paging -> fallback list() 1 lần
                List<UserRepresentation> once = users.list();
                return once == null ? List.of() : once;
            }

            if (page == null || page.isEmpty()) {
                hasMore = false;
            } else {
                all.addAll(page);
                first += KC_PAGE_SIZE;
                hasMore = page.size() == KC_PAGE_SIZE;
            }
        }

        return all;
    }

    private List<UserRepresentation> searchAllUsersPaged(String filter) {
        UsersResource users = getUsersResource();
        List<UserRepresentation> all = new ArrayList<>();

        int first = 0;
        boolean hasMore = true;

        while (hasMore) {
            List<UserRepresentation> page = users.search(filter, first, KC_PAGE_SIZE);

            if (page == null || page.isEmpty()) {
                hasMore = false;
            } else {
                all.addAll(page);
                first += KC_PAGE_SIZE;
                hasMore = page.size() == KC_PAGE_SIZE;
            }
        }

        return all;
    }

    /**
     * Tenant-aware paging fallback (khi multi nhưng org-mode tắt)
     */
    private List<UserRepresentation> searchTenantAwarePage(String filter, int tenantOffset, int size) {
        if (isSingleMode()) {
            return getUsersResource().search(filter, tenantOffset, size);
        }

        TenantPageCollector collector = new TenantPageCollector(tenantOffset, size);

        int first = 0;
        boolean hasMore = true;

        while (collector.needsMore() && hasMore) {
            List<UserRepresentation> page = getUsersResource().search(filter, first, KC_PAGE_SIZE);

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
        if (isSingleMode()) return searchAllUsersPaged(filter).size();
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
        String orgId = resolveOrgIdByAlias(keycloak.realm(realm), orgAlias);
        if (orgId == null || orgId.isBlank()) {
            throw new BusinessException(CommonErrorCode.KEYCLOAK_UNKNOWN_ERROR);
        }
        return orgId;
    }

    private List<UserRepresentation> listOrgMembersPaged(String orgId) {
        if (orgId == null || orgId.isBlank()) return List.of();

        List<UserRepresentation> all = new ArrayList<>();
        String token = keycloak.tokenManager().getAccessTokenString();

        int first = 0;
        boolean hasMore = true;

        while (hasMore) {
            String url = adminRealmBaseUrl()
                    + "/organizations/" + orgId
                    + "/members?first=" + first + "&max=" + KC_PAGE_SIZE;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            ResponseEntity<UserRepresentation[]> resp = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    UserRepresentation[].class
            );

            UserRepresentation[] body = resp.getBody();
            int len = (body == null) ? 0 : body.length;

            if (len == 0) {
                hasMore = false;
            } else {
                all.addAll(Arrays.asList(body));
                first += KC_PAGE_SIZE;
                hasMore = len == KC_PAGE_SIZE;
            }
        }

        return all;
    }

    private boolean isMemberOfOrg(String orgId, String userId) {
        try {
            String token = keycloak.tokenManager().getAccessTokenString();

            String url = adminRealmBaseUrl()
                    + "/organizations/" + orgId
                    + "/members/" + userId;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            ResponseEntity<String> resp = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class
            );

            return resp.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound ex) {
            return false;
        } catch (Exception ex) {
            log.error("Error checking org member orgId={}, userId={}, err={}", orgId, userId, ex.toString());
            return false;
        }
    }

    // Organization resolve alias -> id

    private String resolveOrgIdByAlias(RealmResource realmResource, String orgAlias) {
        String alias = normalizeAlias(orgAlias);
        if (alias == null) return null;

        int max = 200;
        for (int first = 0; first <= 10_000; first += max) {
            List<OrganizationRepresentation> page = loadOrganizationPage(realmResource, first, max);
            if (page.isEmpty()) return null;

            String found = findOrgIdInPage(page, alias);
            if (found != null) return found;

            if (page.size() < max) return null;
        }
        return null;
    }

    private List<OrganizationRepresentation> loadOrganizationPage(RealmResource realmResource, int first, int max) {
        try {
            List<OrganizationRepresentation> page = realmResource.organizations().search("", false, first, max);
            return page == null ? List.of() : page;
        } catch (Exception e) {
            log.warn("organizations().search failed, fallback REST. first={}, max={}, err={}",
                    first, max, e.toString());
            return fetchOrganizationsByRest(first, max);
        }
    }

    private String findOrgIdInPage(List<OrganizationRepresentation> page, String alias) {
        for (OrganizationRepresentation o : page) {
            if (o == null) continue;
            String a = o.getAlias();
            if (a != null && a.equalsIgnoreCase(alias)) {
                return o.getId();
            }
        }
        return null;
    }

    private List<OrganizationRepresentation> fetchOrganizationsByRest(int first, int max) {
        try {
            String url = adminRealmBaseUrl() + "/organizations?first=" + first + "&max=" + max;

            String token = keycloak.tokenManager().getAccessTokenString();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            ResponseEntity<OrganizationRepresentation[]> resp = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    OrganizationRepresentation[].class
            );

            OrganizationRepresentation[] body = resp.getBody();
            return body == null ? List.of() : Arrays.asList(body);
        } catch (HttpClientErrorException.Forbidden ex) {
            log.error("FORBIDDEN when listing organizations. Need service-account roles. err={}", ex.getMessage());
            return List.of();
        } catch (Exception ex) {
            log.error("Error fetching organizations by REST: {}", ex.toString());
            return List.of();
        }
    }

    private String normalizeAlias(String orgAlias) {
        if (orgAlias == null || orgAlias.isBlank()) return null;
        return orgAlias.trim();
    }

    // Keycloak create + error map

    private String createKeycloakUser(UsersResource usersResource, UserRepresentation rep) {
        try (Response response = usersResource.create(rep)) {
            if (response.getStatus() == 201) {
                return requireCreatedUserId(response);
            }
            String errorBody = readEntitySafe(response);
            throw toBusinessException(errorBody);
        }
    }

    private String requireCreatedUserId(Response response) {
        String createdUserId = CreatedResponseUtil.getCreatedId(response);
        if (createdUserId == null || createdUserId.isBlank()) {
            throw new BusinessException(CommonErrorCode.KEYCLOAK_UNKNOWN_ERROR);
        }
        return createdUserId;
    }

    private String requireOrgId(RealmResource realmResource, String orgAlias) {
        String orgId = resolveOrgIdByAlias(realmResource, orgAlias);
        if (orgId == null || orgId.isBlank()) {
            throw new BusinessException(CommonErrorCode.KEYCLOAK_UNKNOWN_ERROR);
        }
        return orgId;
    }

    private void addMemberToOrganization(RealmResource realmResource, String orgId, String userId) {
        try (Response resp = realmResource.organizations().get(orgId).members().addMember(userId)) {
            int st = resp.getStatus();
            if (st != 201 && st != 204) {
                throw new BusinessException(CommonErrorCode.KEYCLOAK_UNKNOWN_ERROR);
            }
        }
    }

    private String readEntitySafe(Response response) {
        try {
            return (response != null && response.hasEntity()) ? response.readEntity(String.class) : "";
        } catch (Exception ignored) {
            return "";
        }
    }

    private BusinessException toBusinessException(String errorBody) {
        KeycloakErrorPayload payload = parseKeycloakErrorPayload(errorBody);
        ErrorCode errorCode = mapToErrorCode(payload.errorKey());

        if (errorCode == CommonErrorCode.KEYCLOAK_MIN_LENGTH) {
            String minLength = extractNumber(payload.errorDescription());
            return new BusinessException(errorCode, minLength);
        }
        return new BusinessException(errorCode);
    }

    private KeycloakErrorPayload parseKeycloakErrorPayload(String errorBody) {
        if (errorBody == null || errorBody.isBlank()) return new KeycloakErrorPayload(null, null);

        try {
            Map<String, Object> errorMap = objectMapper.readValue(errorBody, new TypeReference<>() {});
            return new KeycloakErrorPayload(
                    (String) errorMap.get("error"),
                    (String) errorMap.get("error_description")
            );
        } catch (Exception e) {
            return new KeycloakErrorPayload(null, null);
        }
    }

    private record KeycloakErrorPayload(String errorKey, String errorDescription) {}

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

    private void safeDeleteUser(RealmResource realmResource, String userId) {
        if (userId == null || userId.isBlank()) return;
        try {
            realmResource.users().get(userId).remove();
        } catch (Exception ignored) {
            // best-effort rollback
        }
    }

    private ErrorCode mapToErrorCode(String keycloakErrorKey) {
        if (keycloakErrorKey == null) {
            return CommonErrorCode.KEYCLOAK_UNKNOWN_ERROR;
        }

        return switch (keycloakErrorKey) {
            case KeycloakErrorConstants.ERROR_MIN_LOWER_CASE -> CommonErrorCode.KEYCLOAK_MIN_LOWER_CASE;
            case KeycloakErrorConstants.ERROR_MIN_UPPER_CASE -> CommonErrorCode.KEYCLOAK_MIN_UPPER_CASE;
            case KeycloakErrorConstants.ERROR_MIN_DIGITS -> CommonErrorCode.KEYCLOAK_MIN_DIGITS;
            case KeycloakErrorConstants.ERROR_MIN_SPECIAL_CHARS -> CommonErrorCode.KEYCLOAK_MIN_SPECIAL_CHARS;
            case KeycloakErrorConstants.ERROR_MIN_LENGTH -> CommonErrorCode.KEYCLOAK_MIN_LENGTH;
            default -> CommonErrorCode.KEYCLOAK_UNKNOWN_ERROR;
        };
    }

    private String extractNumber(String text) {
        if (text == null) return "0";
        String number = text.replaceAll("\\D", "");
        return number.isEmpty() ? "0" : number;
    }

    // CONFLICT checks

    private void checkUserExist(List<UserRepresentation> userList, UserDto userDto, boolean isUpdate, String excludeUserId) {
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

        if (usernameExist) throw new BusinessException(ErrorCode.CONFLICT, true);
        if (emailExist) throw new BusinessException(ErrorCode.CONFLICT, true);
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

        if (usernameExist) throw new BusinessException(ErrorCode.CONFLICT, true);
        if (emailExist) throw new BusinessException(ErrorCode.CONFLICT, true);
    }

    private void checkUserIdExist(String userId) {
        try {
            UserRepresentation rep = getUser(userId).toRepresentation();
            ensureUserBelongsToCurrentTenant(rep);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.NOT_FOUND, userId);
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
        if (isUsingDb) {
            for (String roleName : roleNames) {
                try {
                    permissionService.getPermissionByCode(roleName);
                } catch (BusinessException e) {
                    errors.add(roleName);
                }
            }
        } else {
            RolesResource rolesResource = getRolesResource();
            for (String roleName : roleNames) {
                try {
                    rolesResource.get(roleName).toRepresentation();
                } catch (Exception e) {
                    errors.add(roleName);
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(CommonErrorCode.EXISTS, "Role name không tồn tại: " + String.join(", ", errors));
        }
    }

    
    // Keycloak resources
    

    private UsersResource getUsersResource() {
        return keycloak.realm(realm).users();
    }

    private RolesResource getRolesResource() {
        return keycloak.realm(realm).roles();
    }

    private GroupsResource getGroupResource() {
        return keycloak.realm(realm).groups();
    }

    public List<GroupRepresentation> listGroups() {
        GroupsResource groupsResource = getGroupResource();
        List<GroupRepresentation> allGroups = new ArrayList<>();
        List<GroupRepresentation> topGroups = groupsResource.groups();
        for (GroupRepresentation group : topGroups) {
            allGroups.add(group);
            getChildGroupsRecursively(group.getId(), allGroups);
        }
        return allGroups;
    }

    private void getChildGroupsRecursively(String parentId, List<GroupRepresentation> result) {
        List<GroupRepresentation> subGroups = getChildrenGroupsByParentId(parentId);
        for (GroupRepresentation subGroup : subGroups) {
            result.add(subGroup);
            getChildGroupsRecursively(subGroup.getId(), result);
        }
    }

    private List<GroupRepresentation> getChildrenGroupsByParentId(String parentId) {
        String url = adminRealmBaseUrl() + "/groups/" + parentId + "/children";

        String token = ((JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication()).getToken().getTokenValue();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<GroupRepresentation[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    GroupRepresentation[].class
            );
            GroupRepresentation[] body = response.getBody();
            return body == null ? List.of() : Arrays.asList(body);
        } catch (HttpClientErrorException.Forbidden ex) {
            log.error("Permission denied when accessing group children for parentId {}: {}", parentId, ex.getMessage());
            return List.of();
        } catch (Exception ex) {
            log.error("Error when fetching group children for parentId {}: {}", parentId, ex.getMessage());
            return List.of();
        }
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
            if (done || page == null || page.isEmpty()) return;

            for (UserRepresentation u : page) {
                if (done) return;
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
