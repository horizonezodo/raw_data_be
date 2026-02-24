package ngvgroup.com.rpt.features.smrscore.service;

import ngvgroup.com.rpt.features.smrscore.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ResultScoreService {
    Page<BranchScoreCommonInfo> getBranchResult(String keyword, String ciId, String scoreInstanceCode, Pageable pageable);
    List<BranchScoreCommonInfo> exportExcelBranchResult(String keyword, String ciId, String scoreInstanceCode);

    BranchResultDto getBranchResultDetail(Long id);

    Page<SmrScoreSearchDto> search(String keyword, ReqSmrScoreSearchDto dto, Pageable pageable);
    List<SmrScoreExportExcelDto> exportExcel(String keyword, ReqSmrScoreSearchDto dto);

}
