package com.naas.category_service.controller;

import com.naas.category_service.dto.CtgCfgCollateralType.CtgCfgCollateralTypeDto;
import com.naas.category_service.dto.CtgCfgCollateralType.CtgCfgCollateralTypeResponse;
import com.naas.category_service.service.CtgCfgCollateralTypeService;
import com.ngvgroup.bpm.core.dto.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ctg-cfg-collateral-type")
@PreAuthorize("hasRole('category_product_collateral_type')")
public class CtgCfgCollateralTypeController {
    private final CtgCfgCollateralTypeService ctgCfgCollateralTypeService;

    @PostMapping
    public ResponseEntity<ResponseData<Void>> createCtgCfgCollateralType(@RequestBody CtgCfgCollateralTypeDto request) {
        ctgCfgCollateralTypeService.create(request);
        return ResponseData.okEntity();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> updateCtgCfgCollateralType(@PathVariable Long id,  @RequestBody CtgCfgCollateralTypeDto request) {
        ctgCfgCollateralTypeService.update(id, request);
        return ResponseData.okEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteCtgCfgCollateralType(@PathVariable Long id) {
        ctgCfgCollateralTypeService.delete(id);
        return ResponseData.okEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<CtgCfgCollateralTypeDto>> getDetail(@PathVariable Long id) {
        return ResponseData.okEntity(ctgCfgCollateralTypeService.findOne(id));
    }

    @GetMapping
    public ResponseEntity<ResponseData<Page<CtgCfgCollateralTypeResponse>>> search(@RequestParam(required = false) String filter, @ParameterObject Pageable pageable) {
        return ResponseData.okEntity(ctgCfgCollateralTypeService.searchAll(filter, pageable, false));
    }

    @GetMapping("/export-excel")
    public ResponseEntity<ByteArrayResource> exportToExcel(@RequestParam(required = false) String filter,

                                                           @RequestParam List<String> labels) {
        return ctgCfgCollateralTypeService.exportToExcel(filter, labels);
    }

    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String collateralTypeCode) {
        return ResponseData.okEntity(ctgCfgCollateralTypeService.checkExist(collateralTypeCode));
    }

}
