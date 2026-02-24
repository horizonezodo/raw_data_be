package com.ngv.zns_service.controller;

import com.ngv.zns_service.dto.request.SearchFilterRequest;
import com.ngv.zns_service.dto.request.ZSSNDungSearchRequest;
import org.springframework.data.domain.Page;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngv.zns_service.dto.response.ZSSNDungResponse;
import com.ngv.zns_service.service.ZSSNDungService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gui-nhan-tin")
public class ZSSNDungController {
    private final ZSSNDungService zssnDungService;
    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @PostMapping("/search-advanced")
    public ResponseEntity<ResponseData<Page<ZSSNDungResponse>>> findAll(@RequestBody ZSSNDungSearchRequest request) {
        return ResponseData.okEntity(zssnDungService.findAll(request));
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @PostMapping("/search-all")
    public ResponseEntity<ResponseData<Page<ZSSNDungResponse>>> searchAll(@RequestBody SearchFilterRequest request) {
        return ResponseData.okEntity(zssnDungService.searchAll(request));
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_MODIFY)")
    @PostMapping("/export-excel/{fileName}")
    public ResponseEntity<ByteArrayResource> exportToExcel(@PathVariable String fileName, @RequestBody ZSSNDungSearchRequest request) {
        return zssnDungService.exportToExcel(request, fileName);
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @GetMapping("/list-ten-dvu")
    public ResponseEntity<ResponseData<Map<String, String>>> listTenDvu() {
        return ResponseData.okEntity(zssnDungService.listTenDvu());
    }




}
