package com.ngv.zns_service.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngv.zns_service.dto.response.SelectBoxDto;
import com.ngv.zns_service.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/common")
public class CommonController {
    private final CommonService commonService;
    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @GetMapping("/popup")
    public ResponseEntity<ResponseData<Map<String, String>>> listCommon(@RequestParam String maDmuc) {
        return ResponseData.okEntity(commonService.listCommon(maDmuc));
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_VIEW)")
    @GetMapping("/list-mdich-sdung")
    public ResponseEntity<ResponseData<List<SelectBoxDto>>> getListMdichSdung() {
        return ResponseData.okEntity(commonService.getListMdichSdung());
    }

}
