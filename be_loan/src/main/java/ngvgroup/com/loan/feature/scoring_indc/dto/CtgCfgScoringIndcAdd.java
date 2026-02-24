package ngvgroup.com.loan.feature.scoring_indc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.loan.feature.scoring_indc_result.dto.CtgCfgScoringIndcResultDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgScoringIndcAdd {
    private CtgCfgScoringIndcDto ctgCfgScoringIndcDto;
    private List<CtgCfgScoringIndcResultDTO> ctgCfgScoringIndcResultDTOS;
}
