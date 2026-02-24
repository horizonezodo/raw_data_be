package com.naas.admin_service.features.auth.controller;

import com.naas.admin_service.core.utils.SecurityUtils;
import com.naas.admin_service.features.auth.dto.UpdateBranchRequestDto;
import com.naas.admin_service.features.auth.dto.UpdateBranchResponseDto;
import com.naas.admin_service.features.auth.service.BranchService;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.ListResourceMappingDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/branch")
public class BranchController {

    private final BranchService branchService;

    @GetMapping("/list-branch")
    public ResponseEntity<List<ListResourceMappingDto>> getListBranch() {
        List<ListResourceMappingDto> list = branchService.getListByUsername();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/update-branch")
    public ResponseEntity<UpdateBranchResponseDto> updateBranchCode(@RequestBody UpdateBranchRequestDto request) {
        branchService.updateBranchCode(SecurityUtils.getCurrentUsername(), request.getBranchCode());
        return ResponseEntity.ok(new UpdateBranchResponseDto(true, "Updated branch code"));
    }

}
