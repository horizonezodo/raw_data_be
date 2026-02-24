package com.naas.admin_service.features.category.controller;

import com.naas.admin_service.features.category.dto.CtgInfWardDto;
import com.naas.admin_service.features.category.service.CtgInfWardService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ward")
@AllArgsConstructor
@PreAuthorize("hasRole('category_geography_region_ward')")
public class CtgInfWardController {

    private final CtgInfWardService ctgInfWardService;

    @PostMapping("/get-list")
    ResponseEntity<ResponseData<Page<CtgInfWardDto>>> getWards(@ParameterObject Pageable pageable){
        return ResponseData.okEntity(ctgInfWardService.getWards(pageable));
    }

    @GetMapping("/search-list")
    ResponseEntity<ResponseData<Page<CtgInfWardDto>>> searchWards(@RequestParam String keyword, @ParameterObject Pageable pageable){
        return ResponseData.okEntity(ctgInfWardService.searchWards(keyword,pageable));
    }

    @PostMapping("/export-to-excel/{fileName}")
    ResponseEntity<ByteArrayResource> exportToExcel(@RequestParam String filter, @PathVariable String fileName, @RequestBody List<String> labels){
        return ctgInfWardService.exportToExcel(filter,fileName,labels);
    }

    @PostMapping("/create")
    ResponseEntity<ResponseData<Void>> create(@RequestBody CtgInfWardDto ctgInfWardDto){
        ctgInfWardService.create(ctgInfWardDto);
        return ResponseData.createdEntity();
    }

    @PutMapping("/update")
    ResponseEntity<ResponseData<Void>> update(@RequestBody CtgInfWardDto ctgInfWardDto){
        ctgInfWardService.update(ctgInfWardDto);
        return ResponseData.createdEntity();
    }

    @DeleteMapping("/delete/{warCode}")
    ResponseEntity<ResponseData<Void>> delete(@PathVariable String warCode){
        ctgInfWardService.delete(warCode);
        return ResponseData.createdEntity();
    }

    @GetMapping("/detail/{warCode}")
    ResponseEntity<ResponseData<CtgInfWardDto>> getDetail(@PathVariable String warCode){
        return ResponseData.okEntity(ctgInfWardService.getDetail(warCode));
    }

    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String warCode) {
        return ResponseData.okEntity(ctgInfWardService.checkExist(warCode));
    }

    @GetMapping()
    public ResponseEntity<ResponseData<List<CtgInfWardDto>>> getAll() {
        return ResponseData.okEntity(ctgInfWardService.getAll());
    }

}
