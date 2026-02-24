package com.naas.admin_service.features.log.controller;

import com.naas.admin_service.features.log.dto.ComInfLogActivityDto;
import com.naas.admin_service.features.log.dto.LogSearchRequestDto;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.naas.admin_service.features.log.service.ComInfLogActivityService;

import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("log-activity")
@RequiredArgsConstructor
public class ComInfLogActivityController {
    private final ComInfLogActivityService logActivityService;

    private final ExportExcel exportExcel;

    @PostMapping("search")
    public ResponseEntity<ResponseData<Page<ComInfLogActivityDto>>> search(
            @RequestBody LogSearchRequestDto searchRequestDto,
            @RequestParam(required = false) String keyword,
            @ParameterObject Pageable pageable) {
        return ResponseData.okEntity(logActivityService.search(searchRequestDto, keyword, pageable));
    }

    @PostMapping("export-excel")
    public ResponseEntity<byte[]> exportExcel(
            @RequestBody LogSearchRequestDto requestDto,
            @RequestParam(required = false) String keyword,
            @RequestParam String fileName,
            @RequestParam String type, 
            @ParameterObject Pageable pageable) throws Exception {
        if (type.equals("all")) {
            List<ComInfLogActivityDto> data = logActivityService.getAll(requestDto, keyword);
            return exportExcel.exportExcel(data, fileName);
        } else {
            Page<ComInfLogActivityDto> data = logActivityService.search(requestDto, keyword, pageable);
            List<ComInfLogActivityDto> dataList = data.getContent();
            return exportExcel.exportExcel(dataList, fileName);
        }
    }
}
