package com.naas.admin_service.features.job.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.naas.admin_service.features.job.dto.ComLogJobDto;
import com.naas.admin_service.features.job.service.ComLogJobService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("log-jobs")
@RequiredArgsConstructor
public class ComLogJobController {
    private final ComLogJobService logJobService;

    @GetMapping("/search")
    public ResponseEntity<ResponseData<Page<ComLogJobDto>>> search(
            @RequestParam(required = false) String search,
            @ParameterObject Pageable pageable) {
        return ResponseData.okEntity(logJobService.search(search, pageable));
    }
}
