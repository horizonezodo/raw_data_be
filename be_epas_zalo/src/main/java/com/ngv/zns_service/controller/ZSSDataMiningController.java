package com.ngv.zns_service.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngv.zns_service.service.ZSSDataMiningService;
import com.ngv.zns_service.dto.request.SearchFilterRequest;
import com.ngv.zns_service.dto.response.ZSSDataMiningResponse;
import com.ngv.zns_service.dto.request.ZSSDataMiningSearchRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/khai-thac-du-lieu")
public class ZSSDataMiningController {
    private final ZSSDataMiningService zssDataMiningService;

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @PostMapping("/search-advanced")
    public ResponseEntity<ResponseData<Page<ZSSDataMiningResponse>>> findAll(@RequestBody ZSSDataMiningSearchRequest request) {
        return ResponseData.okEntity(zssDataMiningService.findAll(request));
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @PostMapping("/search-all")
    public ResponseEntity<ResponseData<Page<ZSSDataMiningResponse>>> searchAll(@RequestBody SearchFilterRequest request) {
        return ResponseData.okEntity(zssDataMiningService.searchAll(request));
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_MODIFY)")
    @PostMapping("/export-excel/{fileName}")
    public ResponseEntity<ByteArrayResource> exportToExcel(@PathVariable String fileName, @RequestBody ZSSDataMiningSearchRequest request) {
        return zssDataMiningService.exportToExcel(request, fileName);
    }
}
