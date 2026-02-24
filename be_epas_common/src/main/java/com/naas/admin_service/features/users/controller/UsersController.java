package com.naas.admin_service.features.users.controller;

import com.naas.admin_service.core.excel.dto.request.ExportExcelRequest;
import com.naas.admin_service.core.excel.dto.request.SearchFilterRequest;
import com.naas.admin_service.core.excel.dto.response.PageResponse;
import com.naas.admin_service.features.users.dto.InfUserDto;
import com.naas.admin_service.features.users.dto.RoleResponseDto;
import com.naas.admin_service.features.users.dto.UserDto;
import com.naas.admin_service.features.users.service.UserService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;

import java.util.List;

@Slf4j
@LogActivity(function = "Quản lý người dùng")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;

    @PreAuthorize("hasAnyRole('admin_user', 'admin_rule')")
    @GetMapping
    @LogActivity(function = "Lấy danh sách người dùng")
    public ResponseEntity<ResponseData<List<UserRepresentation>>> listUser() {
        List<UserRepresentation> users = userService.listUser();
        return ResponseData.okEntity(users);
    }

    @PreAuthorize("hasRole('admin_user')")
    @PostMapping("/search")
    @LogActivity(function = "Tìm kiếm người dùng")
    public ResponseEntity<ResponseData<PageResponse<UserRepresentation>>> pageUser(
            @RequestBody SearchFilterRequest request) {
        PageResponse<UserRepresentation> users = userService.pageUser(request);
        return ResponseData.okEntity(users);
    }

    @PreAuthorize("hasRole('admin_user')")
    @GetMapping("/{userId}")
    @LogActivity(function = "Lấy thông tin chi tiết người dùng")
    public ResponseEntity<ResponseData<UserRepresentation>> getUser(@PathVariable String userId) {
        UserRepresentation user = userService.getUser(userId).toRepresentation();
        return ResponseData.okEntity(user);
    }

    @PreAuthorize("hasAnyRole('admin_user', 'impl_process_registration_customer', 'impl_process_registration_loan')")
    @PostMapping("/inf-list-user")
    @LogActivity(function = "Lấy danh sách thông tin người dùng theo ID")
    public ResponseEntity<ResponseData<List<InfUserDto>>> getInfListUser(@RequestBody List<String> listUserId) {
        List<InfUserDto> list = userService.getUserInfoList(listUserId);
        return ResponseData.okEntity(list);
    }

    @PreAuthorize("hasAnyRole('admin_user', 'impl_process_registration_customer', 'impl_process_registration_loan')")
    @PostMapping("/inf-list-username")
    @LogActivity(function = "Lấy danh sách thông tin người dùng theo tên đăng nhập")
    public ResponseEntity<ResponseData<List<InfUserDto>>> getUserNameList(@RequestBody List<String> listUserName) {
        List<InfUserDto> list = userService.getUsernameList(listUserName);
        return ResponseData.okEntity(list);
    }

    @PreAuthorize("hasRole('admin_user')")
    @PostMapping
    @LogActivity(function = "Thêm mới người dùng")
    public ResponseEntity<ResponseData<Void>> createUser(@Valid @RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return ResponseData.createdEntity();
    }

    @PreAuthorize("hasRole('admin_user')")
    @PutMapping("/{userId}")
    @LogActivity(function = "Cập nhật thông tin người dùng")
    public ResponseEntity<ResponseData<Void>> updateUser(@PathVariable String userId,
            @RequestBody UserDto userDto) {
        userService.updateUser(userId, userDto);
        return ResponseData.okEntity(null);
    }


    @PreAuthorize("hasRole('admin_user')")
    @DeleteMapping("/{id}")
    @LogActivity(function = "Xóa người dùng")
    public ResponseEntity<ResponseData<Void>> deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return ResponseData.noContentEntity();
    }


    @PreAuthorize("hasRole('admin_user')")
    @GetMapping("/roles/{id}")
    @LogActivity(function = "Lấy danh sách vai trò của người dùng")
    public ResponseEntity<ResponseData<List<RoleResponseDto>>> getUserRoles(@PathVariable String id){
        List<RoleResponseDto> representations = userService.getUserRoles(id);
        return ResponseData.okEntity(representations);
    }

    @PreAuthorize("hasRole('admin_user')")
    @GetMapping("/groups/{id}")
    @LogActivity(function = "Lấy danh sách nhóm của người dùng")
    public ResponseEntity<ResponseData<List<GroupRepresentation>>> getUserGroups(@PathVariable String id) {
        List<GroupRepresentation> groupRepresentations = userService.getUserGroups(id);
        return ResponseData.okEntity(groupRepresentations);
    }

    @PreAuthorize("hasRole('admin_user')")
    @PutMapping("/assign-realm-roles/{userId}")
    @LogActivity(function = "Gán vai trò cho người dùng")
    public ResponseEntity<ResponseData<Void>> assignRole(@PathVariable String userId,
            @RequestBody List<String> roleNames) {
        userService.assignRole(userId, roleNames);
        return ResponseData.okEntity(null);
    }

    @PreAuthorize("hasRole('admin_user')")
    @DeleteMapping("/un-assign/{userId}")
    @LogActivity(function = "Thu hồi vai trò của người dùng")
    public ResponseEntity<ResponseData<Void>> unAssignRole(@PathVariable String userId,
            @RequestBody List<String> roleNames) {
        userService.unAssignRole(userId, roleNames);
        return ResponseData.noContentEntity();
    }

    @PreAuthorize("hasRole('admin_user')")
    @PutMapping("/join-group/{userId}")
    @LogActivity(function = "Thêm người dùng vào nhóm")
    public ResponseEntity<ResponseData<Void>> joinGroup(@PathVariable String userId,
            @RequestBody List<String> groupIds) {
        userService.joinGroups(userId, groupIds);
        return ResponseData.okEntity(null);
    }

    @PreAuthorize("hasRole('admin_user')")
    @DeleteMapping("/leave-group/{userId}")
    @LogActivity(function = "Đưa người dùng rời khỏi nhóm")
    public ResponseEntity<ResponseData<Void>> leaveGroup(@PathVariable String userId,
            @RequestBody List<String> groupIds) {
        userService.leaveGroups(userId, groupIds);
        return ResponseData.noContentEntity();
    }

    @PostMapping("/export-excel/{fileName}")
    @PreAuthorize("hasRole('admin_user')")
    @LogActivity(function = "Xuất Excel danh sách người dùng")
    public ResponseEntity<byte[]> downloadExcel(@PathVariable("fileName") String fileName,
            @RequestBody ExportExcelRequest request) {
        return userService.exportExcel(fileName, request);
    }

    @PostMapping("/change-pass")
    @PreAuthorize("hasRole('admin_user')")
    @LogActivity(function = "Đổi mật khẩu")
    public ResponseEntity<ResponseData<Void>> changePass(@RequestParam("userId") String userId,
            @RequestParam("newPass") String newPass) {
        userService.changePass(userId, newPass);
        return ResponseData.noContentEntity();
    }

}
