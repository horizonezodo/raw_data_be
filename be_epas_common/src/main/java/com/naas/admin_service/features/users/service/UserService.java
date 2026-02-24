package com.naas.admin_service.features.users.service;

import com.naas.admin_service.core.excel.dto.request.ExportExcelRequest;
import com.naas.admin_service.core.excel.dto.request.SearchFilterRequest;
import com.naas.admin_service.core.excel.dto.response.PageResponse;
import com.naas.admin_service.features.users.dto.InfUserDto;
import com.naas.admin_service.features.users.dto.RoleResponseDto;
import com.naas.admin_service.features.users.dto.UserDto;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    void createUser(UserDto userDto);

    void updateUser(String userId, UserDto userDto);


    List<UserRepresentation> listUser();

    PageResponse<UserRepresentation> pageUser(SearchFilterRequest request);

    UserResource getUser(String userId);

    List<InfUserDto> getUserInfoList(List<String> userIds);
    List<InfUserDto> getUsernameList(List<String> listUserName);

    void deleteUser(String userId);

    List<RoleResponseDto> getUserRoles(String userId);

    List<GroupRepresentation> getUserGroups(String userId);

    void assignRole(String userId ,List<String> roleNames);

    void unAssignRole(String userId ,List<String> roleNames);

    void joinGroups(String userId, List<String> groupIds);
    void leaveGroups(String userId, List<String> groupIds);

    ResponseEntity<byte[]> exportExcel(String fileName, ExportExcelRequest request);

    void updateBranchCode(String username, String branchCode);
    String getUserId(String username);

    void changePass(String userName,String newPass);
}
