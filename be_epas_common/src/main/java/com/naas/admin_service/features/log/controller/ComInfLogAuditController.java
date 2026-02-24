package com.naas.admin_service.features.log.controller;

import com.naas.admin_service.features.log.dto.LogSearchRequestDto;
import com.naas.admin_service.features.log.dto.ComInfLogAuditDto;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.naas.admin_service.features.log.service.ComInfLogAuditService;

import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("log-audit")
@RequiredArgsConstructor
public class ComInfLogAuditController {
    private final ComInfLogAuditService logAuditService;
    private final ExportExcel exportExcel;

    @PostMapping("search")
    public ResponseEntity<ResponseData<Page<ComInfLogAuditDto>>> search(
            @RequestBody LogSearchRequestDto requestDto,
            @RequestParam(required = false) String keyword,
            @ParameterObject Pageable pageable) {
        return ResponseData.okEntity(logAuditService.search(requestDto, keyword, pageable));
    }

    @GetMapping()
    public ResponseEntity<ResponseData<Page<ComInfLogAuditDto>>> details(
            @RequestParam Long auditId,
            @RequestParam(required = false) String keyword,
            @ParameterObject Pageable pageable) {
        return ResponseData.okEntity(logAuditService.getDetails(auditId, keyword, pageable));
    }

    @PostMapping("export-excel")
    public ResponseEntity<byte[]> exportExcel(
            @RequestBody LogSearchRequestDto requestDto,
            @RequestParam(required = false) String keyword,
            @RequestParam String fileName,
            @RequestParam String type, 
            @ParameterObject Pageable pageable) throws Exception {
        if (type.equals("all")) {
            List<ComInfLogAuditDto> data = logAuditService.getAll(requestDto, keyword);
            return exportExcel.exportExcel(data, fileName);
        } else {
            Page<ComInfLogAuditDto> data = logAuditService.search(requestDto, keyword, pageable);
            List<ComInfLogAuditDto> dataList = data.getContent();
            return exportExcel.exportExcel(dataList, fileName);
        }
    }
}
