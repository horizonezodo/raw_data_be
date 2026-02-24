package com.naas.admin_service.features.users.service.impl;


import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.core.excel.dto.request.ExportExcelRequest;
import com.naas.admin_service.features.permission.repository.ComCfgPermissionMapRepository;
import com.naas.admin_service.features.permission.service.PermissionService;
import com.naas.admin_service.features.users.dto.ExportExcelGroupDTO;
import com.naas.admin_service.features.users.dto.RoleResponseDto;
import com.naas.admin_service.features.users.service.GroupService;
import com.naas.admin_service.features.users.service.RoleService;
import com.naas.admin_service.features.users.service.UserService;
import com.naas.admin_service.core.excel.service.ExcelService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class GroupServiceImpl implements GroupService {

    private final Keycloak keycloak;

    @Value("${security.keycloak.realm}")
    private String realm;

    @Value("${bpm.core.security.permission.use-db:false}")
    private boolean isUsingDb;

    private final UserService userService;

    private final RoleService roleService;

    private final ExcelService excelService;

    private final PermissionService permissionService;

    private final ComCfgPermissionMapRepository permissionMapRepository;

    @Override
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
        try {
            GroupResource groupResource = getGroupResource().group(parentId);
            GroupRepresentation groupRepresentation = groupResource.toRepresentation();
            List<GroupRepresentation> subGroups = groupRepresentation.getSubGroups();
            return subGroups != null ? subGroups : Collections.emptyList();
        } catch (Exception ex) {
            log.error("Error when fetching group children for parentId {}: {}", parentId, ex.getMessage());
            return Collections.emptyList();
        }
    }


    @Override
    public GroupRepresentation getGroup(String id){
        this.checkGroupIdExist(id);
        GroupsResource groupsResource = getGroupResource();
        return groupsResource.group(id).toRepresentation();
    }


    @Override
    public void createGroup(String groupName){
        List<GroupRepresentation> groupList = listGroups();
        boolean groupNameExist = groupList.stream().anyMatch(group -> group.getName().equals(groupName));
        if (groupNameExist) {
            throw new BusinessException(ErrorCode.CONFLICT,groupName);
        }
        GroupRepresentation groupRepresentation = new GroupRepresentation();
        groupRepresentation.setName(groupName);
        GroupsResource groupsResource = getGroupResource();
        groupsResource.add(groupRepresentation);
    }

    @Override
    public void createChildGroup(String parentGroupId, String childGroupName){
        List<GroupRepresentation> groupList = listGroups();
        boolean groupNameExist = groupList.stream().anyMatch(group -> group.getName().equals(childGroupName));
        if (groupNameExist) {
            throw new BusinessException(ErrorCode.CONFLICT,childGroupName);
        }
        GroupRepresentation childGroup = new GroupRepresentation();
        childGroup.setName(childGroupName);
        childGroup.setParentId(parentGroupId);
        GroupsResource groupsResource = getGroupResource();
        GroupResource parentGroupResource = groupsResource.group(parentGroupId);
        parentGroupResource.subGroup(childGroup);
        GroupRepresentation updatedParentGroup = groupsResource.group(parentGroupId).toRepresentation();
        updatedParentGroup.getSubGroups();
    }

    @Override
    public void updateGroup(String id, String groupName){
        this.checkGroupIdExist(id);
        GroupRepresentation groupRepresentation = new GroupRepresentation();
        groupRepresentation.setName(groupName);
        GroupsResource groupsResource = getGroupResource();
        groupsResource.group(id).update(groupRepresentation);
    }

    @Override
    public void moveGroup(String id, String newParentGroupId){
        this.checkGroupIdExist(id);
        GroupsResource groupsResource = getGroupResource();
        GroupResource sourceGroupResource = groupsResource.group(id);
        GroupRepresentation sourceGroupRepresentation = sourceGroupResource.toRepresentation();
        List<GroupRepresentation> childGroup = getChildrenGroupsByParentId(id);
        List<GroupRepresentation> oldParentChildGroup = getChildrenGroupsByParentId(newParentGroupId);
        sourceGroupRepresentation.setId(null);

        this.checkGroupIdExist(newParentGroupId);
        GroupResource targetParentResource = groupsResource.group(newParentGroupId);
        targetParentResource.subGroup(sourceGroupRepresentation);
        List<GroupRepresentation> newParentChildGroup = getChildrenGroupsByParentId(newParentGroupId);
        Set<String> oldIds = oldParentChildGroup.stream()
                .map(GroupRepresentation::getId)
                .collect(Collectors.toSet());

        List<GroupRepresentation> newGroups = newParentChildGroup.stream()
                .filter(gr -> !oldIds.contains(gr.getId()))
                .toList();

        if(!childGroup.isEmpty()) {
            for (GroupRepresentation gp:childGroup) {
                this.moveGroup(gp.getId(),newGroups.get(0).getId());
            }
        }
        sourceGroupResource.remove();
    }

    @Override
    public void deleteGroup(String id){
        this.checkGroupIdExist(id);
        GroupsResource groupsResource = getGroupResource();
        groupsResource.group(id).remove();
    }

    @Override
    public List<UserRepresentation> listUserByGroup(String id){
        this.checkGroupIdExist(id);
        GroupResource group = getGroupResource().group(id);
        return group.members();
    }

    @Override
    public void addMembersToGroup(List<String> userIds, String id){
        this.checkGroupIdExist(id);
        this.checkUserIdsExist(userIds);
        for (String userId : userIds) {
            UserResource user = getUsersResource().get(userId);
            user.joinGroup(id);
        }
    }

    @Override
    public void leaveMembersToGroup(List<String> userIds, String id){
        this.checkGroupIdExist(id);
        this.checkUserIdsExist(userIds);
        for (String userId : userIds) {
            UserResource user = getUsersResource().get(userId);
            user.leaveGroup(id);
        }
    }

    @Override
    public void addRolesToGroup(List<String> roleNames, String groupId){
        this.checkGroupIdExist(groupId);
        this.checkRoleNamesExist(roleNames);
        if (isUsingDb) {
            permissionService.addRolesToGroup(getGroup(groupId).getName(), roleNames);
        } else {
            List<RoleRepresentation> roles = roleNames.stream()
                    .map(roleName -> getRolesResource().get(roleName).toRepresentation())
                    .toList();
            getGroupResource().group(groupId).roles().realmLevel().add(roles);
        }
    }



    @Override
    public void removeRolesToGroup(List<String> roleNames, String groupId){
        this.checkGroupIdExist(groupId);
        this.checkRoleNamesExist(roleNames);
        if (isUsingDb) {
            permissionService.removeRolesFromGroup(getGroup(groupId).getName(), roleNames);
        } else {
            List<RoleRepresentation> roles = roleNames.stream()
                    .map(roleName -> getRolesResource().get(roleName).toRepresentation())
                    .toList();
            getGroupResource().group(groupId).roles().realmLevel().remove(roles);
        }
    }


    @Override
    public List<RoleResponseDto> listRolesByGroup(String groupId) {
        this.checkGroupIdExist(groupId);
        if (isUsingDb) {
            String groupName = getGroup(groupId).getName();
            return permissionMapRepository.findPermissionInfoByGroupName(groupName);
        } else {
            return getGroupResource().group(groupId).roles().realmLevel().listAll().stream()
                    .map(r -> new RoleResponseDto(r.getName(), r.getDescription()))
                    .toList();
        }
    }

    @Override
    public ResponseEntity<byte[]> exportExcel(String fileName, ExportExcelRequest request) {
        List<GroupRepresentation> res = this.listGroups();
        Map<String, String> idToNameMap = res.stream()
                .collect(Collectors.toMap(GroupRepresentation::getId, GroupRepresentation::getName));
        List<ExportExcelGroupDTO> dtos = res.stream()
                .map(group -> {
                    ExportExcelGroupDTO dto = new ExportExcelGroupDTO();
                    dto.setName(group.getName());
                    if (group.getParentId() != null && idToNameMap.containsKey(group.getParentId())) {
                        dto.setParentName(idToNameMap.get(group.getParentId()));
                    } else {
                        dto.setParentName("");
                    }
                    return dto;
                })
                .toList();
        byte[] response = excelService.exportToExcelContent(dtos, request.getLabels(), ExportExcelGroupDTO.class);
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


    private void checkGroupIdExist(String groupId){
        List<GroupRepresentation> lstGroups = listGroups();

        boolean exists = lstGroups.stream()
                .anyMatch(group -> group.getId().equals(groupId));

        if (!exists) {
            throw new BusinessException(ErrorCode.NOT_FOUND,groupId);
        }
    }


    private void checkUserIdsExist(List<String> userIds){
        List<UserRepresentation> userList = userService.listUser();
        Map<String, UserRepresentation> userMap = userList.stream()
                .collect(Collectors.toMap(UserRepresentation::getId, user -> user));

        List<String> errors = new ArrayList<>();
        for (String userId : userIds) {
            if (!userMap.containsKey(userId)) {
                errors.add(userId);
            }
        }
        if (!errors.isEmpty()) {
            throw new BusinessException(CommonErrorCode.EXISTS,   "User ID không tồn tại: " + String.join(", ", errors));
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
            List<RoleResponseDto> roleList = roleService.listRoles();
            Map<String, RoleResponseDto> roleMap = roleList.stream()
                    .collect(Collectors.toMap(RoleResponseDto::getName, role -> role));
            for (String roleName : roleNames) {
                if (!roleMap.containsKey(roleName)) {
                    errors.add(roleName);
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(CommonErrorCode.EXISTS, "Role name không tồn tại: " + String.join(", ", errors));
        }
    }


    private GroupsResource getGroupResource() {
        return keycloak.realm(realm).groups();
    }

    private UsersResource getUsersResource() {
        return keycloak.realm(realm).users();
    }

    private RolesResource getRolesResource() {
        return keycloak.realm(realm).roles();
    }
}


