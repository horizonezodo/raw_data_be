package ngvgroup.com.loan.feature.scoring_indc_group.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.feature.scoring_indc_group.dto.CtgCfgScoringIndcGroupDtoV2;
import ngvgroup.com.loan.feature.scoring_indc_group.service.CtgCfgScoringIndcGroupService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ctg-cfg-scoring-indc-group")
@RequiredArgsConstructor
public class CtgCfgScoringIndcGroupController {
    private final CtgCfgScoringIndcGroupService ctgCfgScoringIndcGroupService;

    @GetMapping
    public ResponseEntity<ResponseData<Page<CtgCfgScoringIndcGroupDtoV2>>>getAllByTypeCode(@RequestParam("scoringTypeCode") String scoringTypeCode, @RequestParam("keyword") String keyword, @ParameterObject Pageable pageable){
        return ResponseData.okEntity(ctgCfgScoringIndcGroupService.getAllByTypeCode(scoringTypeCode,keyword,pageable));
    }

    @GetMapping("/search-all")
    public ResponseEntity<ResponseData<Page<CtgCfgScoringIndcGroupDtoV2>>> searchAll(
            @RequestParam(name="keyword" , required = false) String keyword,
            @ParameterObject Pageable pageable) {
        return ResponseData.okEntity(ctgCfgScoringIndcGroupService.searchAll(keyword, pageable));
    }

    @PostMapping("/export-to-excel/{fileName}")
    public ResponseEntity<byte[]> exportToExcel(@RequestParam("keyword") String keyword, @PathVariable("fileName") String fileName, @RequestBody List<String> label){
        return ctgCfgScoringIndcGroupService.exportToExcel(keyword,fileName,label);
    }

    @PostMapping
    public ResponseEntity<ResponseData<Void>> create(@RequestBody CtgCfgScoringIndcGroupDtoV2 scoringIndcGroup) {
        ctgCfgScoringIndcGroupService.createScoringIndcGroup(scoringIndcGroup);
        return ResponseData.createdEntity();
    }

    @PutMapping
    public ResponseEntity<ResponseData<Void>> update(@RequestBody CtgCfgScoringIndcGroupDtoV2 scoringIndcGroup) {
        ctgCfgScoringIndcGroupService.updateScoringIndcGroup(scoringIndcGroup);
        return ResponseData.noContentEntity();
    }

    @DeleteMapping
    public ResponseEntity<ResponseData<Void>> delete(@RequestParam("scoringIndcGroupCode") String scoringIndcGroupCode) {
        ctgCfgScoringIndcGroupService.deleteScoringIndcGroup(scoringIndcGroupCode);
        return ResponseData.noContentEntity();
    }

    @GetMapping("/get-detail")
    public ResponseEntity<ResponseData<CtgCfgScoringIndcGroupDtoV2>> getDetail(@RequestParam("scoringIndcGroupCode") String scoringIndcGroupCode) {
        return ResponseData.okEntity(ctgCfgScoringIndcGroupService.getDetailScoringIndcGroup(scoringIndcGroupCode));
    }

    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam("scoringIndcGroupCode") String scoringIndcGroupCode) {
        return ResponseData.okEntity(ctgCfgScoringIndcGroupService.checkExist(scoringIndcGroupCode));
    }
}
