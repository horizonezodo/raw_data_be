package com.naas.category_service.controller;

import com.naas.category_service.dto.CtgCfgLoanPurpose.CtgCfgLoanPurposeDto;
import com.naas.category_service.dto.CtgCfgLoanPurpose.CtgCfgLoanPurposeResponse;
import com.naas.category_service.service.CtgCfgLoanPurposeService;
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
@RequestMapping("/ctg-cfg-loan-purpose")
@PreAuthorize("hasRole('category_product_loan_purpose')")
public class CtgCfgLoanPurposeController {
    private final CtgCfgLoanPurposeService ctgCfgLoanPurposeService;

    @PostMapping
    public ResponseEntity<ResponseData<Void>> createCtgCfgCollateralType(@RequestBody CtgCfgLoanPurposeDto request) {
        ctgCfgLoanPurposeService.create(request);
        return ResponseData.okEntity();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> updateCtgCfgCollateralType(@PathVariable Long id,  @RequestBody CtgCfgLoanPurposeDto request) {
        ctgCfgLoanPurposeService.update(id, request);
        return ResponseData.okEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteCtgCfgCollateralType(@PathVariable Long id) {
        ctgCfgLoanPurposeService.delete(id);
        return ResponseData.okEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<CtgCfgLoanPurposeDto>> getDetail(@PathVariable Long id) {
        return ResponseData.okEntity(ctgCfgLoanPurposeService.findOne(id));
    }

    @GetMapping
    public ResponseEntity<ResponseData<Page<CtgCfgLoanPurposeResponse>>> search(@RequestParam(required = false) String filter,@ParameterObject Pageable pageable) {
        return ResponseData.okEntity(ctgCfgLoanPurposeService.searchAll(filter, pageable,false));
    }

    @GetMapping("/export-excel")
    public ResponseEntity<ByteArrayResource> exportToExcel(@RequestParam(required = false) String filter,

                                                           @RequestParam List<String> labels) {
        return ctgCfgLoanPurposeService.exportToExcel(filter, labels);
    }

    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String purposeCode) {
        return ResponseData.okEntity(ctgCfgLoanPurposeService.checkExist(purposeCode));
    }
}
