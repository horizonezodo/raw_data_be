package com.naas.category_service.controller;

import com.naas.category_service.dto.CtgInfEconomicType.CtgInfEconomicTypeDto;
import com.naas.category_service.dto.CtgInfEconomicType.CtgInfEconomicTypeResponse;
import com.naas.category_service.service.CtgInfEconomicTypeService;
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
@RequestMapping("/ctg-inf-economic-type")
@PreAuthorize("hasRole('category_product_economic_type')")
public class CtgInfEconomicTypeController {
    private final CtgInfEconomicTypeService ctgInfEconomicTypeService;

    @PostMapping
    public ResponseEntity<ResponseData<Void>> createCtgCfgCollateralType(@RequestBody CtgInfEconomicTypeDto request) {
        ctgInfEconomicTypeService.create(request);
        return ResponseData.okEntity();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> updateCtgCfgCollateralType(@PathVariable Long id,  @RequestBody CtgInfEconomicTypeDto request) {
        ctgInfEconomicTypeService.update(id, request);
        return ResponseData.okEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteCtgCfgCollateralType(@PathVariable Long id) {
        ctgInfEconomicTypeService.delete(id);
        return ResponseData.okEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<CtgInfEconomicTypeDto>> getDetail(@PathVariable Long id) {
        return ResponseData.okEntity(ctgInfEconomicTypeService.findOne(id));
    }

    @GetMapping
    public ResponseEntity<ResponseData<Page<CtgInfEconomicTypeResponse>>> search(@RequestParam(required = false) String filter, @ParameterObject Pageable pageable) {
        return ResponseData.okEntity(ctgInfEconomicTypeService.searchAll(filter, pageable,false));
    }

    @GetMapping("/export-excel")
    public ResponseEntity<ByteArrayResource> exportToExcel(@RequestParam(required = false) String filter,
                                                           @RequestParam List<String> labels) {
        return ctgInfEconomicTypeService.exportToExcel(filter, labels);
    }

    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String economicTypeCode) {
        return ResponseData.okEntity(ctgInfEconomicTypeService.checkExist(economicTypeCode));
    }
}
