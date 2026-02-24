package com.naas.admin_service.features.area.controller;

import com.naas.admin_service.features.category.dto.CtgInfWardDto;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import com.naas.admin_service.features.area.dto.ComInfAreaDto;
import com.naas.admin_service.features.area.dto.ComInfAreaRequestDto;
import com.naas.admin_service.features.area.dto.ComInfAreaResponse;
import com.naas.admin_service.features.area.model.ComInfArea;
import com.naas.admin_service.features.area.service.ComInfAreaService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/com-area")
@AllArgsConstructor
public class ComInfAreaController {
    private final ComInfAreaService ctgComAreaService;

    @Operation(
            summary = "Danh sách khu vực"
    )
    @GetMapping("/get-all")
    public ResponseEntity<ResponseData<Page<ComInfAreaDto>>> getAll(@RequestParam("keyword") String keyword,
                                                                    @RequestParam("orgCode") String orgCode,
                                                                    @ParameterObject Pageable pageable) {
        Page<ComInfAreaDto> res = ctgComAreaService.getCtgComAreas(keyword, orgCode, pageable);
        return ResponseData.okEntity(res);
    }

    @PostMapping("/search")
    public ResponseEntity<ResponseData<Page<ComInfAreaResponse>>> search(@RequestBody ComInfAreaRequestDto request, Pageable pageable) {
        return ResponseData.okEntity(ctgComAreaService.searchAll(request, pageable));
    }

    @PostMapping
    @PreAuthorize("hasRole('category_geography_region')")
    public ResponseEntity<ResponseData<Void>> create(@RequestBody ComInfAreaRequestDto request) {
        ctgComAreaService.create(request);
        return ResponseData.createdEntity();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('category_geography_region')")
    public ResponseEntity<ResponseData<Void>> update(
            @PathVariable("id") Long id,
            @RequestBody ComInfAreaRequestDto request) {
        ctgComAreaService.update(id, request);
        return ResponseData.createdEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<ComInfAreaRequestDto>> getDetail(@PathVariable("id") Long id) {
        return ResponseData.okEntity(ctgComAreaService.findOne(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('category_geography_region')")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable("id") Long id) {
        ctgComAreaService.delete(id);
        return ResponseData.noContentEntity();
    }

    @PostMapping("/export-excel/{fileName}")
    @PreAuthorize("hasRole('category_geography_region')")
    public ResponseEntity<byte[]> exportExcel(
            @RequestBody ComInfAreaRequestDto request,
            @PathVariable("fileName") String fileName) {
        return ctgComAreaService.exportToExcel(request, fileName);
    }

    @GetMapping("/get-all-by-org-codes")
    public ResponseEntity<ResponseData<List<ComInfArea>>> getAllByOrgCodes(@RequestParam("orgCodes") List<String> orgCodes) {
        return ResponseData.okEntity(ctgComAreaService.getCtgComAreasByOrgCodes(orgCodes));
    }

    @GetMapping("/get-by-area-code")
    public ResponseEntity<ResponseData<String>> getAreaName(@RequestParam("areaCode") String areaCode) {
        return ResponseData.okEntity(ctgComAreaService.getByAreaCode(areaCode));
    }

    @GetMapping("/get-list-ward")
    public ResponseEntity<ResponseData<List<CtgInfWardDto>>> getListWard(@RequestParam("orgCode") String orgCode) {
        return ResponseData.okEntity(ctgComAreaService.getListWard(orgCode));
    }

    @GetMapping("/get-list-area")
    public ResponseEntity<ResponseData<List<ComInfAreaDto>>> getListAreaByWard() {
        return ResponseData.okEntity(ctgComAreaService.getListAreaByWard());
    }


    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam("areaCode") String areaCode) {
        return ResponseData.okEntity(ctgComAreaService.checkExist(areaCode));
    }



}
