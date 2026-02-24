package com.naas.admin_service.features.category.controller;

import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.naas.admin_service.features.category.dto.CtgInfProvinceDto;
import com.naas.admin_service.features.category.service.CtgInfProvinceService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;

import java.util.List;

@RestController
@RequestMapping("/province")
@AllArgsConstructor
@PreAuthorize("hasRole('category_geography_region_province')")
public class CtgInfProvinceController {
    private final CtgInfProvinceService ctgInfProvinceService;


    @GetMapping("/get-list")
    ResponseEntity<ResponseData<Page<CtgInfProvinceDto>>> getProvinces(@ParameterObject Pageable pageable){
        return ResponseData.okEntity(ctgInfProvinceService.getProvinces(pageable));
    }

    @GetMapping("/search-list")
    ResponseEntity<ResponseData<Page<CtgInfProvinceDto>>> findProvinces(@RequestParam String keyword, @ParameterObject Pageable pageable){
        return ResponseData.okEntity(ctgInfProvinceService.findProvinces(keyword, pageable));
    }

    @PostMapping("/export-to-excel/{fileName}")
    ResponseEntity<ByteArrayResource> exportToExcel(@RequestParam String filter, @PathVariable String fileName, @RequestBody List<String> label){
        return ctgInfProvinceService.exportToExcel(filter,fileName,label);
    }

    @GetMapping("/detail/{provinceCode}")
    ResponseEntity<ResponseData<CtgInfProvinceDto>> getProvinceDetail(@PathVariable String provinceCode){
        return ResponseData.okEntity(ctgInfProvinceService.getDetailProvince(provinceCode));
    }

    @PostMapping("/create")
    ResponseEntity<ResponseData<Void>> createProvince(@RequestBody CtgInfProvinceDto ctgInfProvinceDto){
        ctgInfProvinceService.createProvince(ctgInfProvinceDto);
        return ResponseData.createdEntity();
    }

    @PutMapping("/update")
    ResponseEntity<ResponseData<Void>> updateProvince(@RequestBody CtgInfProvinceDto ctgInfProvinceDto){
        ctgInfProvinceService.updateProvince(ctgInfProvinceDto);
        return ResponseData.createdEntity();
    }

    @DeleteMapping("/delete/{provinceCode}")
    ResponseEntity<ResponseData<Void>> deleteProvince(@PathVariable String provinceCode){
        ctgInfProvinceService.deleteProvince(provinceCode);
        return ResponseData.createdEntity();
    }

    @GetMapping("/get-all")
    @PreAuthorize("permitAll()")
    ResponseEntity<ResponseData<List<CtgInfProvinceDto>>> getAll(){
        return ResponseData.okEntity(ctgInfProvinceService.getAll());
    }


    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String provinceCode) {
        return ResponseData.okEntity(ctgInfProvinceService.checkExist(provinceCode));
    }



}
