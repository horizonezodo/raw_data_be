package ngvgroup.com.rpt.features.smrtxnscore.service;

import ngvgroup.com.rpt.features.smrscore.dto.BranchResultDto;
import ngvgroup.com.rpt.features.smrtxnscore.dto.ScoreCalculationRequestDto;
import ngvgroup.com.rpt.features.smrtxnscore.dto.ScoreRequestDto;

import java.util.List;

public interface ScoreCalculationService {
    List<BranchResultDto> calculatePreview(List<String> keywords, ScoreCalculationRequestDto req);
    void saveScore(ScoreRequestDto req, List<BranchResultDto> calculatedBranches, String makerUserCode, String makerUserName);
    String generateScoreInstanceCode();

}
