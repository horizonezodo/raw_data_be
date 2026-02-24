package com.naas.category_service.controller;

import com.naas.category_service.dto.ExportExcelDTO;
import com.naas.category_service.dto.CtgInfPosition.ExportCtgInfPositionDTO;
import com.naas.category_service.dto.CtgInfPosition.CtgInfPositionDTO;
import com.naas.category_service.dto.ResponseData;
import com.naas.category_service.service.CtgInfPositionService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/position")
@PreAuthorize("hasRole('category_common_position')")
public class CtgInfPositionController {
    private final CtgInfPositionService service;

    @GetMapping
    public ResponseEntity<ResponseData<List<ExportCtgInfPositionDTO>>> getAll(){
        return ResponseData.okEntity(service.listPosition());
    }

    @PostMapping
    public ResponseEntity<ResponseData<Page<CtgInfPositionDTO>>> search(@RequestParam String keyword, @ParameterObject Pageable pageable){
       Page<CtgInfPositionDTO> page = service.search(keyword, pageable);
        return ResponseData.okEntity(page);
    }

    @GetMapping("/{positionCode}")
    public ResponseEntity<ResponseData<CtgInfPositionDTO>> getDetail(@PathVariable String positionCode){
        return ResponseData.okEntity(service.getDetail(positionCode));
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseData<Void>> create(@RequestBody CtgInfPositionDTO dto){
        service.createPosition(dto);
        return ResponseData.createdEntity();
    }

    @PutMapping("/update/{positionCode}")
    public ResponseEntity<ResponseData<Void>> update(@PathVariable String positionCode,@RequestBody CtgInfPositionDTO dto){
        service.updatePosition(positionCode,dto);
        return ResponseData.noContentEntity();
    }

    @DeleteMapping("/delete/{positionCode}")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable String positionCode){
        service.deletePosition(positionCode);
        return ResponseData.noContentEntity();
    }

    @PostMapping("/export")
    public ResponseEntity<ByteArrayResource> exportData(@RequestBody ExportExcelDTO dto){
        return service.exportExcel(dto);
    }

    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String positionCode) {
        return ResponseData.okEntity(service.checkExist(positionCode));
    }

}
