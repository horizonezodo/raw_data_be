package com.naas.admin_service.features.category.controller;

import com.naas.admin_service.features.category.dto.CtgInfCreditInstDTO;
import com.naas.admin_service.features.category.service.CtgInfCreditInstService;
import com.naas.admin_service.features.category.dto.ExportExcelDTO;
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
@RequestMapping("/credit")
@RequiredArgsConstructor
@PreAuthorize("hasRole('category_common_credit_inst')")
public class CtgInfCreditInstController {
    private final CtgInfCreditInstService service;

    @PostMapping
    public ResponseEntity<ResponseData<Page<CtgInfCreditInstDTO>>> search(@RequestParam String keyword, @ParameterObject Pageable pageable){
        Page<CtgInfCreditInstDTO> page = service.search(keyword, pageable);
        return ResponseData.okEntity(page);
    }

    @GetMapping("/{creditInstCode}")
    public ResponseEntity<ResponseData<CtgInfCreditInstDTO>> getDetail(@PathVariable String creditInstCode){
        return ResponseData.okEntity(service.getDetail(creditInstCode));
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseData<Void>> create(@RequestBody CtgInfCreditInstDTO dto){
        service.createCreditInst(dto);
        return ResponseData.createdEntity();
    }

    @PutMapping("/update/{creditInstCode}")
    public ResponseEntity<ResponseData<Void>> update(@PathVariable String creditInstCode,@RequestBody CtgInfCreditInstDTO dto){
        service.updateCreditInst(dto,creditInstCode);
        return ResponseData.noContentEntity();
    }

    @DeleteMapping("/delete/{creditInstCode}")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable String creditInstCode){
        service.deleteCredistInst(creditInstCode);
        return ResponseData.noContentEntity();
    }

    @PostMapping("/export")
    public ResponseEntity<ByteArrayResource> exportData(@RequestBody ExportExcelDTO dto){
        return service.exportExcel(dto);
    }

    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String creditInstCode) {
        return ResponseData.okEntity(service.checkExist(creditInstCode));
    }
}
