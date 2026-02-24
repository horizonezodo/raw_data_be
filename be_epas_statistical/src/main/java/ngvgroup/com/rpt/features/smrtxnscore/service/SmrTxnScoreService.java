package ngvgroup.com.rpt.features.smrtxnscore.service;

import ngvgroup.com.rpt.features.smrscore.dto.BranchResultDto;
import ngvgroup.com.rpt.features.smrscore.dto.BranchScoreCommonInfo;
import ngvgroup.com.rpt.features.smrtxnscore.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface SmrTxnScoreService {
    void createScore(SmrTxnScoreDTO dto);

    Page<SmrTxnScorePageDTO> pageScore(String keyword, SearchFilterDTO dto, Pageable pageable);

    List<SmrTxnScoreExportExcelDTO> exportExcel(String keyword, SearchFilterDTO dto);

    SmrTxnScoreDetailDTO getDetailScore(String scoreInstanceCode);

    void changeStatus(Long id, ChangeStatusDto dto);

    List<NextStepDto> getNextSteps(Long id);

    Page<BranchScoreCommonInfo> getBranch(String keyword, String ciId, String scoreInstanceCode, Pageable pageable);

    List<BranchScoreCommonInfo> exportExcelTxnScoreBranch(String keyword, String ciId, String scoreInstanceCode);

    BranchResultDto getBranchDetail(Long id);
}
