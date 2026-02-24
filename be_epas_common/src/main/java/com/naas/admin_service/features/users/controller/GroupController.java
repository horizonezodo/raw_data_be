package com.naas.admin_service.features.users.controller;

import com.naas.admin_service.core.excel.dto.request.ExportExcelRequest;
import com.naas.admin_service.features.users.dto.RoleResponseDto;
import com.naas.admin_service.features.users.service.GroupService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;

@LogActivity(function = "Quản lý nhóm")
@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PreAuthorize("hasRole('admin_group_user')")
    @GetMapping
    @LogActivity(function = "Lấy danh sách nhóm")
    public ResponseEntity<ResponseData<List<GroupRepresentation>>> listGroups() {
        List<GroupRepresentation> groups = groupService.listGroups();
        return ResponseData.okEntity(groups);
    }

    @PreAuthorize("hasRole('admin_group_user')")
    @GetMapping("/{id}")
    @LogActivity(function = "Lấy thông tin chi tiết nhóm")
    public ResponseEntity<ResponseData<GroupRepresentation>> getGroup(@PathVariable String id) {
        GroupRepresentation group = groupService.getGroup(id);
        return ResponseData.okEntity(group);
    }

    @PreAuthorize("hasRole('admin_group_user')")
    @PostMapping
    @LogActivity(function = "Thêm mới nhóm")
    public ResponseEntity<ResponseData<Void>> createGroup(@RequestParam String groupName) {
        groupService.createGroup(groupName);
        return ResponseData.createdEntity();
    }

    @PreAuthorize("hasRole('admin_group_user')")
    @PostMapping("/create-child")
    @LogActivity(function = "Thêm nhóm con")
    public ResponseEntity<ResponseData<Void>> createChildGroup(@RequestParam String groupName, @RequestParam String parentGroupId) {
        groupService.createChildGroup(parentGroupId,groupName);
        return ResponseData.createdEntity();
    }

    @PreAuthorize("hasRole('admin_group_user')")
    @PutMapping("/{id}")
    @LogActivity(function = "Cập nhật thông tin nhóm")
    public ResponseEntity<ResponseData<Void>> updateGroup(@PathVariable String id, @RequestParam String groupName) {
        groupService.updateGroup(id, groupName);
        return ResponseData.okEntity(null);
    }

    @PreAuthorize("hasRole('admin_group_user')")
    @PutMapping("/move-group/{id}")
    @LogActivity(function = "Di chuyển nhóm")
    public ResponseEntity<ResponseData<Void>> moveGroup(@PathVariable String id,
            @RequestParam String newParentGroupId) {
        groupService.moveGroup(id, newParentGroupId);
        return ResponseData.okEntity(null);
    }

    @PreAuthorize("hasRole('admin_group_user')")
    @DeleteMapping("/{id}")
    @LogActivity(function = "Xóa nhóm")
    public ResponseEntity<ResponseData<Void>> deleteGroup(@PathVariable String id) {
        groupService.deleteGroup(id);
        return ResponseData.noContentEntity();
    }

    @PreAuthorize("hasRole('admin_group_user')")
    @GetMapping("/list-user-by-group/{groupId}")
    @LogActivity(function = "Lấy danh sách người dùng trong nhóm")
    public ResponseEntity<ResponseData<List<UserRepresentation>>> listUserByGroup(@PathVariable String groupId) {
        List<UserRepresentation> users = groupService.listUserByGroup(groupId);
        return ResponseData.okEntity(users);
    }

    @PreAuthorize("hasRole('admin_group_user')")
    @PostMapping("/add-members-to-group/{groupId}")
    @LogActivity(function = "Thêm nhóm con")
    public ResponseEntity<ResponseData<Void>> addMembersToGroup(@PathVariable String groupId, @RequestBody List<String> userIds) {
        groupService.addMembersToGroup(userIds, groupId);
        return ResponseData.createdEntity();
    }

    @PreAuthorize("hasRole('admin_group_user')")
    @DeleteMapping("/leave-members-to-group/{groupId}")
    @LogActivity(function = "Xóa thành viên khỏi nhóm")
    public ResponseEntity<ResponseData<Void>> leaveMembersToGroup(@PathVariable String groupId,
            @RequestBody List<String> userIds) {
        groupService.leaveMembersToGroup(userIds, groupId);
        return ResponseData.noContentEntity();
    }

    @PreAuthorize("hasRole('admin_group_user')")
    @PostMapping("/add-roles-to-group/{groupId}")
    @LogActivity(function = "Thêm vai trò cho nhóm")
    public ResponseEntity<ResponseData<Void>> addRolesToGroup(@PathVariable String groupId,
            @RequestBody List<String> roleNames) {
        groupService.addRolesToGroup(roleNames, groupId);
        return ResponseData.createdEntity();
    }

    @PreAuthorize("hasRole('admin_group_user')")
    @DeleteMapping("/remove-roles-to-group/{groupId}")
    @LogActivity(function = "Xóa vai trò khỏi nhóm")
    public ResponseEntity<ResponseData<Void>> removeRolesToGroup(@PathVariable String groupId,
            @RequestBody List<String> roleNames) {
        groupService.removeRolesToGroup(roleNames, groupId);
        return ResponseData.noContentEntity();
    }

    @PreAuthorize("hasRole('admin_group_user')")
    @GetMapping("/list-roles-by-group/{groupId}")
    @LogActivity(function = "Lấy danh sách vai trò của nhóm")
    public ResponseEntity<ResponseData<List<RoleResponseDto>>> listRolesByGroup(@PathVariable String groupId) {
        List<RoleResponseDto> roles = groupService.listRolesByGroup(groupId);
        return ResponseData.okEntity(roles);
    }

    @PostMapping("/export-excel/{fileName}")
    @PreAuthorize("hasRole('admin_group_user')")
    @LogActivity(function = "Xuất Excel danh sách nhóm")
    public ResponseEntity<byte[]> downloadExcel(@PathVariable("fileName") String fileName,
            @RequestBody ExportExcelRequest request) {
        return groupService.exportExcel(fileName, request);
    }

}
