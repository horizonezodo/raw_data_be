package com.ngv.zns_service.controller;

import com.ngv.zns_service.dto.request.*;
import org.springframework.data.domain.Page;
import com.ngv.zns_service.dto.response.ZSSAppResponse;
import com.ngv.zns_service.exception.ValidationException;
import com.ngv.zns_service.service.ZSSAppService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/app")
public class ZSSAppController {
    private final ZSSAppService zssAppService;

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_MODIFY)")
    @PostMapping
    public ResponseEntity<ResponseData<Void>> create(@Valid @RequestBody ZSSAppRequest request) throws ValidationException {
        zssAppService.createZssApp(request);
        return ResponseData.createdEntity();
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_MODIFY)")
    @PutMapping("/{appId}")
    public ResponseEntity<ResponseData<Void>> update(@RequestBody ZSSAppRequest request) throws ValidationException {
        zssAppService.updateZssApp(request);
        return ResponseData.okEntity(null);
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_DELETE)")
    @DeleteMapping("/{appId}")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable String appId) throws ValidationException {
        zssAppService.deleteZssApp(appId);
        return ResponseData.noContentEntity();
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @GetMapping("/{appId}")
    public ResponseEntity<ResponseData<ZSSAppResponse>> getZSSApp(@PathVariable String appId) {
        ZSSAppResponse zssAppResponse = zssAppService.getZssApp(appId);
        return ResponseData.okEntity(zssAppResponse);
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @PostMapping("/search-all")
    public ResponseEntity<ResponseData<Page<ZSSAppResponse>>> searchAll(@RequestBody SearchFilterRequest request) {
        return ResponseData.okEntity(zssAppService.searchAll(request));
    }

     @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_MODIFY)")
    @PostMapping("/export-excel/{fileName}")
    public ResponseEntity<ByteArrayResource> exportToExcel(@PathVariable String fileName, @RequestBody ZSSAppSearchRequest request) {
        return zssAppService.exportToExcel(request, fileName);
    }
}
