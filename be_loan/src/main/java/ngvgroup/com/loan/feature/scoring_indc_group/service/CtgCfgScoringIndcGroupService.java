package ngvgroup.com.loan.feature.scoring_indc_group.service;

import ngvgroup.com.loan.feature.scoring_indc_group.dto.CtgCfgScoringIndcGroupDtoV2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface CtgCfgScoringIndcGroupService {
    Page<CtgCfgScoringIndcGroupDtoV2> getAllByTypeCode(String scoringTypeCode, String keyword, Pageable pageable);

    Page<CtgCfgScoringIndcGroupDtoV2> searchAll(String keyword, Pageable pageable);

    ResponseEntity<byte[]> exportToExcel(String keyword, String fileName, List<String> labels);

    void createScoringIndcGroup(CtgCfgScoringIndcGroupDtoV2 scoringIndcGroupDto);

    void updateScoringIndcGroup(CtgCfgScoringIndcGroupDtoV2 scoringIndcGroupDto);

    void deleteScoringIndcGroup(String scoringIndcGroupCode);

    CtgCfgScoringIndcGroupDtoV2 getDetailScoringIndcGroup(String scoringIndcGroupCode);

    boolean checkExist(String code);

}
