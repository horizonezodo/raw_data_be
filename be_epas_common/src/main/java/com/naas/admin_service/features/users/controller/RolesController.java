package com.naas.admin_service.features.users.controller;


import com.naas.admin_service.features.users.dto.RoleResponseDto;
import com.naas.admin_service.features.users.service.RoleService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;

import java.util.List;

@LogActivity(function = "Quản lý vai trò")
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RolesController {


    private final RoleService roleService;

    @PreAuthorize("hasRole('admin_role')")
    @GetMapping
    @LogActivity(function = "Lấy danh sách vai trò")
    public ResponseEntity<ResponseData<List<RoleResponseDto>>> listRoles() {
        List<RoleResponseDto> roles = roleService.listRoles();
        return ResponseData.okEntity(roles);
    }

    @PreAuthorize("hasRole('admin_role')")
    @GetMapping("/{roleName}")
    @LogActivity(function = "Lấy thông tin chi tiết vai trò")
    public ResponseEntity<ResponseData<RoleResponseDto>> getRole(@PathVariable String roleName){
        RoleResponseDto role = roleService.getRole(roleName);
        return ResponseData.okEntity(role);
    }

    @PreAuthorize("hasRole('admin_role')")
    @PostMapping
    @LogActivity(function = "Thêm mới vai trò")
    public ResponseEntity<ResponseData<Void>> createRole(@RequestParam String roleName,
            @RequestParam(required = false) String description) {
        roleService.createRole(roleName, description);
        return ResponseData.createdEntity();
    }

    @PreAuthorize("hasRole('admin_role')")
    @PutMapping
    @LogActivity(function = "Cập nhật vai trò")
    public ResponseEntity<ResponseData<Void>> updateRole(@RequestParam String roleName,
            @RequestParam(required = false) String description) {
        roleService.updateRole(roleName, description);
        return ResponseData.okEntity(null);
    }

    @PreAuthorize("hasRole('admin_role')")
    @DeleteMapping("/{roleName}")
    @LogActivity(function = "Xóa vai trò")
    public ResponseEntity<ResponseData<Void>> deleteRole(@PathVariable String roleName) {
        roleService.deleteRole(roleName);
        return ResponseData.noContentEntity();
    }

    @PreAuthorize("hasRole('admin_role')")
    @GetMapping("/list-user-by-role/{roleName}")
    @LogActivity(function = "Lấy danh sách người dùng theo vai trò")
    public ResponseEntity<ResponseData<List<UserRepresentation>>> listUserByRole(@PathVariable String roleName) {
        List<UserRepresentation> users = roleService.listUserByRole(roleName);
        return ResponseData.okEntity(users);
    }
}
