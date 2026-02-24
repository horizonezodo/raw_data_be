package com.naas.category_service.controller;

import com.naas.category_service.dto.CtgCfgInterestRateDtl.CtgCfgInterestRateDtlDto;
import com.naas.category_service.model.CtgCfgInterestRateDtl;
import com.naas.category_service.service.CtgCfgInterestRateDtlService;
import com.ngvgroup.bpm.core.dto.ResponseData;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interest-rate-dtl")
@AllArgsConstructor
public class CtgCfgInterestRateDtlController {
    private CtgCfgInterestRateDtlService ctgCfgInterestRateDtlService;

    @PostMapping
    public ResponseEntity<ResponseData<Void>> create(@RequestBody List<CtgCfgInterestRateDtlDto> ctgCfgInterestRateDtlDtos ) {
        ctgCfgInterestRateDtlService.createInterestRateDtl(ctgCfgInterestRateDtlDtos);
        return ResponseData.createdEntity();
    }

    @PutMapping
    public ResponseEntity<ResponseData<Void>> update(@RequestBody List<CtgCfgInterestRateDtlDto> ctgCfgInterestRateDtlDtos){
        ctgCfgInterestRateDtlService.updateInterestRateDtl(ctgCfgInterestRateDtlDtos);
        return ResponseData.createdEntity();
    }

    @DeleteMapping
    public ResponseEntity<ResponseData<Void>> delete(@RequestParam String interestCode){
        ctgCfgInterestRateDtlService.deleteInterestRateDtl(interestCode);
        return ResponseData.okEntity();
    }

    @DeleteMapping("/delete-by-id")
    public ResponseEntity<ResponseData<Void>> deleteById(@RequestParam Long id){
        ctgCfgInterestRateDtlService.deleteById(id);
        return ResponseData.okEntity();
    }

    @GetMapping("/get-detail")
    public ResponseEntity<ResponseData<Page<CtgCfgInterestRateDtlDto>>> getDetail(@RequestParam(required = false) String interestCode , @RequestParam(required = false) String keyword, @ParameterObject Pageable pageable){
        return ResponseData.okEntity(ctgCfgInterestRateDtlService.getDetailInterestRateDtls(interestCode, keyword, pageable));
    }


    @GetMapping("/get-all")
    public  ResponseEntity<ResponseData<List<CtgCfgInterestRateDtlDto>>> getAll(@RequestParam(required = false) String interestCode , @RequestParam(required = false) String keyword){
        return ResponseData.okEntity(ctgCfgInterestRateDtlService.getAll(interestCode,keyword));
    }


}
