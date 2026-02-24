package com.naas.category_service.controller;

import com.naas.category_service.dto.ExportExcelDTO;
import com.naas.category_service.dto.CtgInfTitle.CtgInfTitleDTO;
import com.naas.category_service.dto.ResponseData;
import com.naas.category_service.service.CtgInfTitleService;
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

    @GetMapping("/{TitleCode}")
    public ResponseEntity<ResponseData<CtgInfTitleDTO>> getDetail(@PathVariable String TitleCode){
        return ResponseData.okEntity(service.getDetail(TitleCode));
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseData<Void>> create(@RequestBody CtgInfTitleDTO dto){
        service.createTitle(dto);
        return ResponseData.createdEntity();
    }

    @PutMapping("/update/{TitleCode}")
    public ResponseEntity<ResponseData<Void>> update(@PathVariable String TitleCode, @RequestBody CtgInfTitleDTO dto){
        service.updateTitle(TitleCode,dto);
        return ResponseData.noContentEntity();
    }

    @DeleteMapping("/delete/{TitleCode}")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable String TitleCode){
        service.deleteTitle(TitleCode);
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
