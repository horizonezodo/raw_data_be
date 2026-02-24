package com.naas.category_service.controller;

import com.naas.category_service.dto.CtgCfgScoringIndcGroup.CtgCfgScoringIndcGroupDto;
import com.naas.category_service.service.CtgCfgScoringIndcGroupService;
import com.ngvgroup.bpm.core.dto.ResponseData;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scoring-indc-group")
@AllArgsConstructor
public class CtgCfgScoringIndcGroupController {

    private final CtgCfgScoringIndcGroupService scoringIndcGroupService;

    @GetMapping
    public ResponseEntity<ResponseData<Page<CtgCfgScoringIndcGroupDto>>> searchAll(
            @RequestParam(required = false) String keyword,
            @ParameterObject Pageable pageable) {
        return ResponseData.okEntity(scoringIndcGroupService.searchAll(keyword, pageable));
    }

    @PostMapping("/export-to-excel/{fileName}")
    public ResponseEntity<ByteArrayResource> exportToExcel(@RequestParam String keyword, @PathVariable String fileName, @RequestBody List<String> label){
        return scoringIndcGroupService.exportToExcel(keyword,fileName,label);
    }

    @PostMapping
    public ResponseEntity<ResponseData<Void>> create(@RequestBody CtgCfgScoringIndcGroupDto scoringIndcGroup) {
        scoringIndcGroupService.createScoringIndcGroup(scoringIndcGroup);
        return ResponseData.createdEntity();
    }

    @PutMapping
    public ResponseEntity<ResponseData<Void>> update(@RequestBody CtgCfgScoringIndcGroupDto scoringIndcGroup) {
        scoringIndcGroupService.updateScoringIndcGroup(scoringIndcGroup);
        return ResponseData.okEntity();
    }

    @DeleteMapping
    public ResponseEntity<ResponseData<Void>> delete(@RequestParam String scoringIndcGroupCode) {
        scoringIndcGroupService.deleteScoringIndcGroup(scoringIndcGroupCode);
        return ResponseData.okEntity();
    }

    @GetMapping("/get-detail")
    public ResponseEntity<ResponseData<CtgCfgScoringIndcGroupDto>> getDetail(@RequestParam String scoringIndcGroupCode) {
        return ResponseData.okEntity(scoringIndcGroupService.getDetailScoringIndcGroup(scoringIndcGroupCode));
    }

    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String scoringIndcGroupCode) {
        return ResponseData.okEntity(scoringIndcGroupService.checkExist(scoringIndcGroupCode));
    }
}
