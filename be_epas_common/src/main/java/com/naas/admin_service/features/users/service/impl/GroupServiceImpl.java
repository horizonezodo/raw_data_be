package com.naas.admin_service.features.users.service.impl;

import com.naas.admin_service.core.excel.dto.request.ExportExcelRequest;
import com.naas.admin_service.features.users.dto.ExportExcelGroupDTO;
import com.naas.admin_service.features.users.dto.RoleResponseDto;
import com.naas.admin_service.features.users.service.GroupService;
import com.naas.admin_service.core.excel.service.ExcelService;
import com.naas.admin_service.core.provider.IdentityStoreService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class GroupServiceImpl implements GroupService {

    private final IdentityStoreService identityStoreService;

    private final ExcelService excelService;

    @Override
    public List<GroupRepresentation> listGroups() {
        return identityStoreService.listGroups();
    }

    @Override
    public GroupRepresentation getGroup(String id) {
        return identityStoreService.getGroup(id);
    }

    @Override
    public void createGroup(String groupName) {
        identityStoreService.createGroup(groupName);
    }

    @Override
    public void createChildGroup(String parentGroupId, String childGroupName) {
        identityStoreService.createChildGroup(parentGroupId, childGroupName);
    }

    @Override
    public void updateGroup(String id, String groupName) {
        identityStoreService.updateGroup(id, groupName);
    }

    @Override
    public void moveGroup(String id, String newParentGroupId) {
        identityStoreService.moveGroup(id, newParentGroupId);
    }

    @Override
    public void deleteGroup(String id) {
        identityStoreService.deleteGroup(id);
    }

    @Override
    public List<UserRepresentation> listUserByGroup(String id) {
        return identityStoreService.listUserByGroup(id);
    }

    @Override
    public void addMembersToGroup(List<String> userIds, String id) {
        identityStoreService.addMembersToGroup(userIds, id);
    }

    @Override
    public void leaveMembersToGroup(List<String> userIds, String id) {
        identityStoreService.leaveMembersToGroup(userIds, id);
    }

    @Override
    public void addRolesToGroup(List<String> roleNames, String groupId) {
        identityStoreService.addRolesToGroup(groupId, roleNames);
    }

    @Override
    public void removeRolesToGroup(List<String> roleNames, String groupId) {
        identityStoreService.removeRolesFromGroup(groupId, roleNames);
    }

    @Override
    public List<RoleResponseDto> listRolesByGroup(String groupId) {
        return identityStoreService.listRolesByGroup(groupId);
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

}
