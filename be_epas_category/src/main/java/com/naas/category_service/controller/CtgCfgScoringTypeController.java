package com.naas.category_service.controller;

import com.naas.category_service.dto.CtgCfgScoringType.CtgCfgScoringTypeDto;

import com.naas.category_service.service.CtgCfgScoringTypeService;
import com.ngvgroup.bpm.core.dto.ResponseData;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/scoring-type")
@AllArgsConstructor
public class CtgCfgScoringTypeController {

    private final CtgCfgScoringTypeService ctgCfgScoringTypeService;

    @GetMapping("/get-all")
    public ResponseEntity<ResponseData<List<CtgCfgScoringTypeDto>>> getAllCtgCfgScoringTypes(){
        return ResponseData.okEntity(ctgCfgScoringTypeService.getAll());
    }

    @GetMapping
    public ResponseEntity<ResponseData<Page<CtgCfgScoringTypeDto>>> searchAll(@RequestParam String keyword,@ParameterObject Pageable pageable){
        return ResponseData.okEntity(ctgCfgScoringTypeService.searchAll(keyword,pageable));
    }

    @PostMapping("/export-to-excel/{fileName}")
    public ResponseEntity<ByteArrayResource> exportToExcel(@RequestParam String keyword, @PathVariable String fileName, @RequestBody List<String> label){
        return ctgCfgScoringTypeService.exportToExcel(keyword,fileName,label);
    }

    @PostMapping
    public ResponseEntity<ResponseData<Void>> create(@RequestBody CtgCfgScoringTypeDto ctgCfgScoringTypeDto){
        ctgCfgScoringTypeService.create(ctgCfgScoringTypeDto);
        return ResponseData.createdEntity();
    }

    @PutMapping
    public ResponseEntity<ResponseData<Void>> update(@RequestBody CtgCfgScoringTypeDto ctgCfgScoringTypeDto){
        ctgCfgScoringTypeService.update(ctgCfgScoringTypeDto);
        return ResponseData.createdEntity();
    }

    @DeleteMapping
    public ResponseEntity<ResponseData<Void>> delete(@RequestParam String scoringTypeCode){
        ctgCfgScoringTypeService.delete(scoringTypeCode);
        return ResponseData.okEntity();
    }


    @PostMapping("/upload-file")
    public ResponseEntity<ResponseData<Void>> uploadFile(@RequestParam(value = "file",required = false) MultipartFile file,@RequestParam String objectCode) throws Exception{
        ctgCfgScoringTypeService.uploadFile(file,objectCode);
        return ResponseData.createdEntity();
    }

    @PostMapping("/download-file")
    public ResponseEntity<byte[]> downloadFile(@RequestParam String objectCode) throws Exception{
        return ctgCfgScoringTypeService.downloadFile(objectCode);
    }

    @DeleteMapping("/delete-file")
    public ResponseEntity<ResponseData<Void>> deleteFile(@RequestParam String objectCode) throws Exception{
        ctgCfgScoringTypeService.deleteFile(objectCode);
        return ResponseData.okEntity();
    }

    @GetMapping("/get-detail")
    public ResponseEntity<ResponseData<CtgCfgScoringTypeDto>> getDetail(@RequestParam String scoringTypeCode){
        return ResponseData.okEntity(ctgCfgScoringTypeService.getDetail(scoringTypeCode));
    }


    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String scoringTypeCode) {
        return ResponseData.okEntity(ctgCfgScoringTypeService.checkExist(scoringTypeCode));
    }

}
