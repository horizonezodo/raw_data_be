package com.ngv.zns_service.controller;

import com.ngv.zns_service.dto.request.SearchFilterRequest;
import com.ngv.zns_service.dto.request.ZssTKhoanZoaSearchRequest;
import org.springframework.data.domain.Page;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngv.zns_service.dto.response.ZssTKhoanZoaResponse;
import com.ngv.zns_service.service.ZssTKhoanZoaService;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/zoa")
public class ZssTKhoanZoaController {

    private final ZssTKhoanZoaService zssTKhoanZoaService;

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @PostMapping("/search-all")
    public ResponseEntity<ResponseData<Page<ZssTKhoanZoaResponse>>> searchAll(
            @RequestBody SearchFilterRequest request) {
        return ResponseData.okEntity(zssTKhoanZoaService.searchAll(request));
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_MODIFY)")
    @PostMapping("/export-excel/{fileName}")
    public ResponseEntity<ByteArrayResource> exportToExcel(@PathVariable String fileName,
            @RequestBody ZssTKhoanZoaSearchRequest request) {
        return zssTKhoanZoaService.exportToExcel(request, fileName);
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @GetMapping("/list-all-ten-dvi")
    public ResponseEntity<ResponseData<Map<String, String>>> listTenDviAll() {
        return ResponseData.okEntity(zssTKhoanZoaService.listTenDviAll());
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_MODIFY)")
    @DeleteMapping("/{maZoa}")
    public ResponseEntity<ResponseData<String>> deleteZoa(@PathVariable String maZoa) {
        zssTKhoanZoaService.deleteZoa(maZoa);
        return ResponseData.okEntity("ZssTKhoanZoa deleted successfully.");
    }

}
