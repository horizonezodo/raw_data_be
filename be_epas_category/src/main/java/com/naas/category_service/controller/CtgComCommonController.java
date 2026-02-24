package com.naas.category_service.controller;

import com.naas.category_service.dto.ComCommon.CommonDto;
import com.naas.category_service.service.CtgComCommonService;
import com.ngvgroup.bpm.core.dto.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/common")
@AllArgsConstructor
public class CtgComCommonController {
    private final CtgComCommonService ctgComCommonService;

    @Operation(
            summary = "Common dùng chung"
    )
    @GetMapping
    public ResponseEntity<ResponseData<List<CommonDto>>> findAll(@RequestParam("commonTypeCode") String commonTypeCode) {
        List<CommonDto> listCommon = ctgComCommonService.listCommon(commonTypeCode);
        return ResponseData.okEntity(listCommon);
    }


}
