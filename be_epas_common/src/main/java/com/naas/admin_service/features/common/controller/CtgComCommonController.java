package com.naas.admin_service.features.common.controller;

import com.naas.admin_service.features.common.dto.CtgComCommonDTO;
import com.naas.admin_service.features.common.service.CtgComCommonService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/common")
public class CtgComCommonController {
    private final CtgComCommonService ctgComCommonService;

    @Operation(summary = "Lấy danh sách common theo common type code", description = "Lấy danh sách common theo common type code", responses = {
    })
    @GetMapping("/type-code/{commonTypeCode}")
    public ResponseEntity<ResponseData<List<CtgComCommonDTO>>> getAllByCommonTypeCode(@PathVariable String commonTypeCode) {
        List<CtgComCommonDTO> ctgComCommonDTOS = ctgComCommonService.findAllByCommonTypeCode(commonTypeCode);
        return ResponseData.okEntity(ctgComCommonDTOS);
    }
}
