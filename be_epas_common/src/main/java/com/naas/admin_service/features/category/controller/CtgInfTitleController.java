package com.naas.admin_service.features.category.controller;

import com.naas.admin_service.features.category.dto.ExportExcelDTO;
import com.naas.admin_service.features.category.dto.CtgInfTitleDTO;
import com.naas.admin_service.features.category.service.CtgInfTitleService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/title")
@RequiredArgsConstructor
@PreAuthorize("hasRole('category_common_position_title')")
public class CtgInfTitleController {
    private final CtgInfTitleService service;

    @PostMapping
    public ResponseEntity<ResponseData<Page<CtgInfTitleDTO>>> search(@RequestParam String keyword, @ParameterObject Pageable pageable){
        Page<CtgInfTitleDTO> page = service.search(keyword, pageable);
        return ResponseData.okEntity(page);
    }

    @GetMapping("/{titleCode}")
    public ResponseEntity<ResponseData<CtgInfTitleDTO>> getDetail(@PathVariable String titleCode){
        return ResponseData.okEntity(service.getDetail(titleCode));
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseData<Void>> create(@RequestBody CtgInfTitleDTO dto){
        service.createTitle(dto);
        return ResponseData.createdEntity();
    }

    @PutMapping("/update/{titleCode}")
    public ResponseEntity<ResponseData<Void>> update(@PathVariable String titleCode, @RequestBody CtgInfTitleDTO dto){
        service.updateTitle(titleCode,dto);
        return ResponseData.noContentEntity();
    }

    @DeleteMapping("/delete/{titleCode}")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable String titleCode){
        service.deleteTitle(titleCode);
        return ResponseData.noContentEntity();
    }

    @PostMapping("/export")
    public ResponseEntity<ByteArrayResource> exportData(@RequestBody ExportExcelDTO dto){
        return service.exportExcel(dto);
    }

    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String titleCode) {
        return ResponseData.okEntity(service.checkExist(titleCode));
    }
}
