package com.naas.admin_service.features.users.controller;

import com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.CtgCfgResourceMappingDto1;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.ListResourceMappingDto;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.ResourceMappingDto;
import com.naas.admin_service.features.users.model.CtgCfgResourceMapping;
import com.naas.admin_service.features.users.service.CtgCfgResourceMappingService;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemaster.CtgCfgResourceMappingRequest;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemaster.CtgCfgResourceMasterDtoV2;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemaster.SearchResourceRequest;
import com.naas.admin_service.features.users.service.CtgCfgResourceMasterService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@LogActivity(function = "Quản lý ánh xạ tài nguyên")
@RestController
@RequiredArgsConstructor
@RequestMapping("/com-cfg-resource-mapping")
public class CtgCfgResourceMappingController {
    private final CtgCfgResourceMappingService ctgCfgResourceMappingService;
    private final CtgCfgResourceMasterService ctgCfgResourceMasterService;

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('admin_resource_type')")
    @LogActivity(function = "Tạo ánh xạ tài nguyên")
    public ResponseEntity<ResponseData<Void>> createComCfgResourceMapping(
            @RequestBody List<CtgCfgResourceMappingRequest> request, @PathVariable("id") String userId) {
        ctgCfgResourceMappingService.updateCfgResourceMapping(request, userId);
        return ResponseData.createdEntity();
    }

    @PostMapping("/group/{groupId}")
    @PreAuthorize("hasRole('admin_resource_type')")
    @LogActivity(function = "Cập nhật ánh xạ tài nguyên theo nhóm")
    public ResponseEntity<ResponseData<Void>> updateGroupComCfgResourceMapping(
            @RequestBody List<CtgCfgResourceMappingRequest> request, @PathVariable("groupId") String groupId) {
        ctgCfgResourceMappingService.updateGroupCfgResourceMapping(request, groupId);
        return ResponseData.createdEntity();
    }

    @GetMapping("/get-list")
    @LogActivity(function = "Lấy danh sách ánh xạ tài nguyên")
    public ResponseEntity<ResponseData<List<ResourceMappingDto>>> getList(
            @RequestParam("resourceTypeCode") String resourceTypeCode) {
        return ResponseData.okEntity(ctgCfgResourceMappingService.getListOfResourceMappingDto(resourceTypeCode));
    }

    @GetMapping("/{resourceTypeCode}")
    @LogActivity(function = "Lấy tất cả ánh xạ tài nguyên theo mã loại")
    public ResponseEntity<ResponseData<List<CtgCfgResourceMapping>>> getAllComCfgResourceMappingsByResourceTypeCode(
            @PathVariable String resourceTypeCode) {
        return ResponseData.okEntity(ctgCfgResourceMappingService.findByResourceTypeCode(resourceTypeCode));
    }

    @PostMapping("/get-active")
    @LogActivity(function = "Lấy danh sách ánh xạ tài nguyên đang hoạt động")
    public ResponseEntity<ResponseData<Page<CtgCfgResourceMasterDtoV2>>> getActiveComCfgResourceMappings(
            @RequestBody SearchResourceRequest searchDTO) {
        return ResponseData.okEntity(ctgCfgResourceMasterService.getAllComCfgResourceMasterActive(searchDTO));
    }

    @Operation(summary = "Danh sách chi nhánh")
    @GetMapping("/get-org")
    @LogActivity(function = "Lấy danh sách đơn vị")
    public ResponseEntity<ResponseData<List<CtgCfgResourceMappingDto1>>> listOrg() {
        return ResponseData.okEntity(ctgCfgResourceMappingService.listOrg());
    }

    @Operation(summary = "Danh sách khu vực")
    @GetMapping("/get-area")
    @LogActivity(function = "Lấy danh sách khu vực")
    public ResponseEntity<ResponseData<List<CtgCfgResourceMappingDto1>>> listArea(
            @RequestParam(required = false) String resourceCode) {
        return ResponseData.okEntity(ctgCfgResourceMappingService.listArea(resourceCode));
    }

    @GetMapping("/resource-type-code")
    @LogActivity(function = "Lấy thông tin ánh xạ tài nguyên")
    public ResponseEntity<ResponseData<List<CtgCfgResourceMapping>>> getResourceMapping(
            @RequestParam(value = "resourceTypeCode") String resourceTypeCode) {
        return ResponseData.okEntity(ctgCfgResourceMappingService.findByResourceTypeCode(resourceTypeCode));
    }

    @GetMapping("/areas")
    @LogActivity(function = "Lấy danh sách khu vực theo loại và đơn vị")
    public ResponseEntity<ResponseData<List<CtgCfgResourceMappingDto1>>> getAreas(
            @RequestParam(value = "resourceTypeCode") String resourceTypeCode,
            @RequestParam(value = "orgCode") String orgCode) {
        return ResponseData
                .okEntity(ctgCfgResourceMappingService.findAreaByResourceTypeName(resourceTypeCode, orgCode));
    }

    @PostMapping("/all/{id}")
    @LogActivity(function = "Lấy tất cả tài nguyên")
    public ResponseEntity<ResponseData<List<CtgCfgResourceMasterDtoV2>>> getAllResource(
            @RequestBody SearchResourceRequest searchDTO, @PathVariable("id") long id, @RequestParam String userId) {
        return ResponseData.okEntity(ctgCfgResourceMasterService.getAllResource(id, searchDTO, userId));
    }

    @PostMapping("/all-group/{id}")
    @LogActivity(function = "Lấy tất cả tài nguyên nhóm")
    public ResponseEntity<ResponseData<List<CtgCfgResourceMasterDtoV2>>> getAllGroupResource(
            @RequestBody SearchResourceRequest searchDTO, @PathVariable("id") long id, @RequestParam String groupId) {
        return ResponseData.okEntity(ctgCfgResourceMasterService.getAllGroupResource(id, searchDTO, groupId));
    }

    @GetMapping("/list-branch/{userId}")
    @LogActivity(function = "Lấy danh sách chi nhánh")
    public ResponseEntity<ResponseData<List<ListResourceMappingDto>>> getAllGroupResource(
            @PathVariable("userId") String userId) {
        return ResponseData.okEntity(ctgCfgResourceMasterService.getListBranch(userId));
    }

    @GetMapping("/list-branch")
    @LogActivity(function = "Lấy danh sách chi nhánh (current user)")
    public ResponseEntity<ResponseData<List<ListResourceMappingDto>>> getListBranchForCurrentUser() {
        return ResponseData.okEntity(ctgCfgResourceMasterService.getListBranch(null));
    }
}
