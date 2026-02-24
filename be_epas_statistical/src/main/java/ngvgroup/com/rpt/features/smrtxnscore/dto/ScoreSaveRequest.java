package ngvgroup.com.rpt.features.smrtxnscore.dto;

import lombok.Getter;
import lombok.Setter;
import ngvgroup.com.rpt.features.smrscore.dto.BranchResultDto;

import java.util.List;

@Getter
@Setter
public class ScoreSaveRequest {
    private ScoreRequestDto req;
    private List<BranchResultDto> branchResults;
    private String makerUserCode;
    private String makerUserName;
}
