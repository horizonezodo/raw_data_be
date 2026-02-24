package ngvgroup.com.loan.feature.scoring_indc_result.service;


import ngvgroup.com.loan.feature.scoring_indc_result.dto.CtgCfgScoringIndcResultDTO;

import java.util.List;

public interface CtgCfgScoringIndcResultService {
    void save(List<CtgCfgScoringIndcResultDTO> dto, String indicatorCode);
    void update(List<CtgCfgScoringIndcResultDTO> dto,String indicatorCode);
    void delete(String indicatorCode);
    List<CtgCfgScoringIndcResultDTO> getAllResult(String indicatorCode);
}
