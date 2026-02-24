package com.naas.admin_service.features.users.service;

import com.naas.admin_service.features.users.dto.RoleResponseDto;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;

import com.naas.admin_service.core.excel.dto.request.ExportExcelRequest;

import java.util.List;

public interface GroupService {

    List<GroupRepresentation> listGroups();
    GroupRepresentation getGroup(String id) ;


    void createGroup(String groupName) ;
    void createChildGroup(String parentGroupId, String childGroupName) ;

    void updateGroup(String id, String groupName) ;
    void moveGroup(String id, String newParentGroupId) ;
    void deleteGroup(String id) ;

    List<UserRepresentation> listUserByGroup(String groupId) ;

    void addMembersToGroup(List<String> userIds, String id) ;
    void leaveMembersToGroup(List<String> userIds, String id) ;

    void addRolesToGroup(List<String> roleNames, String groupId) ;

    void removeRolesToGroup(List<String> roleNames, String groupId) ;
    List<RoleResponseDto> listRolesByGroup(String groupId) ;
    ResponseEntity<byte[]> exportExcel(String fileName, ExportExcelRequest request);

}
