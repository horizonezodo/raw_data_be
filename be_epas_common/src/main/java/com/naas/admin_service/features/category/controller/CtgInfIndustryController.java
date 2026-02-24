package com.naas.admin_service.features.category.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.naas.admin_service.features.category.dto.CtgInfIndustryDto;
import com.naas.admin_service.features.category.dto.CtgInfIndustryResponse;
import com.naas.admin_service.features.category.service.CtgInfIndustryService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ctg-inf-industry")
@PreAuthorize("hasRole('category_product_inf_industry')")
public class CtgInfIndustryController {
    private final CtgInfIndustryService ctgInfIndustryService;

    @PostMapping
    public ResponseEntity<ResponseData<Void>> createCtgCfgCollateralType(@RequestBody CtgInfIndustryDto request) {
        ctgInfIndustryService.create(request);
        return ResponseData.okEntity();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> updateCtgCfgCollateralType(@PathVariable Long id,  @RequestBody CtgInfIndustryDto request) {
        ctgInfIndustryService.update(id, request);
        return ResponseData.okEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteCtgCfgCollateralType(@PathVariable Long id) {
        ctgInfIndustryService.delete(id);
        return ResponseData.okEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<CtgInfIndustryDto>> getDetail(@PathVariable Long id) {
        return ResponseData.okEntity(ctgInfIndustryService.findOne(id));
    }

    @GetMapping
    public ResponseEntity<ResponseData<Page<CtgInfIndustryResponse>>> search(@RequestParam(required = false) String filter, Pageable pageable) {
        return ResponseData.okEntity(ctgInfIndustryService.searchAll(filter, pageable,false));
    }

    @GetMapping("/export-excel")
    public ResponseEntity<ByteArrayResource> exportToExcel(@RequestParam(required = false) String filter,
                                                           @RequestParam List<String> labels) {
        return ctgInfIndustryService.exportToExcel(filter, labels);
    }

    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String industryCode) {
        return ResponseData.okEntity(ctgInfIndustryService.checkInfIndustryCodeExist(industryCode));
    }

    @GetMapping("/get-by-industry-code")
    public ResponseEntity<ResponseData<CtgInfIndustryDto>>getByIndustryCode(@RequestParam String industryCode){
        return  ResponseData.okEntity(ctgInfIndustryService.getByIndustryCode(industryCode));
    }

    @GetMapping("/get-all")
    public ResponseEntity<ResponseData<List<CtgInfIndustryDto>>> getAll(){
        return ResponseData.okEntity(ctgInfIndustryService.getAll());
    }
}
