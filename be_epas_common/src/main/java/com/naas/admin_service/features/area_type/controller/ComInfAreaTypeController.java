package com.naas.admin_service.features.area_type.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.naas.admin_service.features.area_type.dto.ComInfAreaTypeDto;
import com.naas.admin_service.features.area_type.service.ComInfAreaTypeService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/area-type")
public class ComInfAreaTypeController {

    private final ComInfAreaTypeService comInfAreaTypeService;

    public ComInfAreaTypeController(ComInfAreaTypeService comInfAreaTypeService) {
        this.comInfAreaTypeService = comInfAreaTypeService;
    }

    @PostMapping("/get-all")
    public ResponseEntity<ResponseData<Page<ComInfAreaTypeDto>>> getAreaTypes(@RequestParam Pageable pageable) {

        return ResponseData.okEntity(comInfAreaTypeService.getAreaTypes(pageable));
    }

    @GetMapping("/find")
    public ResponseEntity<ResponseData<Page<ComInfAreaTypeDto>>> findAreaTypes(@RequestParam("keyword") String keyword, @ParameterObject Pageable pageable) {

        return ResponseData.okEntity(comInfAreaTypeService.findAreaTypes(keyword, pageable));
    }

    @PostMapping("/exportToExcel/{fileName}")
    @PreAuthorize("hasRole('category_geography_region_type')")
    public ResponseEntity<byte[]> exportToExcel(@RequestParam("keyword") String keyword, @PathVariable("fileName") String fileName, @RequestBody List<String> labels) {
        return comInfAreaTypeService.exportToExcel(keyword, fileName,labels);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('category_geography_region_type')")
    public ResponseEntity<ResponseData<Void>> createAreaType(@RequestBody ComInfAreaTypeDto comInfAreaTypeDto) {
        comInfAreaTypeService.createCtgComAreaType(comInfAreaTypeDto);
        return ResponseData.createdEntity();
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('category_geography_region_type')")
    public ResponseEntity<ResponseData<Void>> updateAreaType(@RequestBody ComInfAreaTypeDto comInfAreaTypeDto) {
        comInfAreaTypeService.updateCtgComAreaType(comInfAreaTypeDto);
        return ResponseData.okEntity(null);
    }

    @DeleteMapping("/delete/{areaTypeCode}")
    @PreAuthorize("hasRole('category_geography_region_type')")
    public ResponseEntity<ResponseData<Void>> deleteAreaType(@PathVariable("areaTypeCode") String areaTypeCode) {
        comInfAreaTypeService.deleteCtgComAreaType(areaTypeCode);
        return ResponseData.okEntity(null);
    }

    @GetMapping("/get-detail/{areaTypeCode}")
    public ResponseEntity<ResponseData<ComInfAreaTypeDto>> getDetail(@PathVariable("areaTypeCode") String areaTypeCode) {
        return ResponseData.okEntity(comInfAreaTypeService.getDetail(areaTypeCode));
    }

    @GetMapping
    public ResponseEntity<ResponseData<List<ComInfAreaTypeDto>>> getAll() {
        return ResponseData.okEntity(comInfAreaTypeService.getAll());
    }

    @GetMapping("/get-distinct-area-type")
    public ResponseEntity<ResponseData<List<ComInfAreaTypeDto>>> getDistinctAreaTypes() {
        return ResponseData.okEntity(comInfAreaTypeService.getDistinctAreaTypes());
    }


}
