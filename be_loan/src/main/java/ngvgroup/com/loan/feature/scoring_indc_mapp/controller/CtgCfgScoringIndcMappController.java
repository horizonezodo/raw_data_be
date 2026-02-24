package ngvgroup.com.loan.feature.scoring_indc_mapp.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.feature.scoring_indc.dto.CtgCfgScoringIndcDto;
import ngvgroup.com.loan.feature.scoring_indc_mapp.service.CtgCfgScoringIndcMappService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ctg-cfg-scoring-indc-mapp")
public class CtgCfgScoringIndcMappController {
    private final CtgCfgScoringIndcMappService ctgCfgScoringIndcMappService;


    @PostMapping
    public ResponseEntity<ResponseData<Void>>createScoringIndcMapp(@RequestBody Map<String, List<CtgCfgScoringIndcDto>> dtos){
        ctgCfgScoringIndcMappService.createIndcMapp(dtos);
        return ResponseData.createdEntity();
    }
    @PostMapping("/delete-by-group-code")
    public ResponseEntity<ResponseData<Void>> deleteByGroupCode(@RequestParam String groupCode){
        ctgCfgScoringIndcMappService.deleteByGroupCode(groupCode);
        return ResponseData.okEntity();
    }
}
