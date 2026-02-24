package ngvgroup.com.rpt.features.ctgcfgstat.service;

import ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatscorekpiresult.CtgCfgStatScoreKpiResultDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CtgCfgStatScoreKpiResultService {

    Page<CtgCfgStatScoreKpiResultDto> searchAll(String kpiCode,String keyword, Pageable pageable);

    void create(List<CtgCfgStatScoreKpiResultDto> ctgCfgStatScoreKpiResultDtos);

    void delete(String kpiCode);

    CtgCfgStatScoreKpiResultDto getDetail(Long id);
}
