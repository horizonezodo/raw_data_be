package ngvgroup.com.loan.feature.scoring_indc_mapp.service;



import ngvgroup.com.loan.feature.scoring_indc.dto.CtgCfgScoringIndcDto;

import java.util.List;
import java.util.Map;

public interface CtgCfgScoringIndcMappService {
    void createIndcMapp(Map<String, List<CtgCfgScoringIndcDto>> dtos);
    void deleteByGroupCode(String groupCode);
}
