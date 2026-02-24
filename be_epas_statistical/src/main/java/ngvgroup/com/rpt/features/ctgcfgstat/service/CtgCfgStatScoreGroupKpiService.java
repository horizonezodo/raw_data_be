package ngvgroup.com.rpt.features.ctgcfgstat.service;

import ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatscoregroupkpi.CtgCfgStatScoreGroupKpiDto;

import java.util.List;

public interface CtgCfgStatScoreGroupKpiService {
    void create(List<CtgCfgStatScoreGroupKpiDto> ctgCfgStatScoreGroupKpiDtos);

    void delete(String statScoreGroupCode);

    List<CtgCfgStatScoreGroupKpiDto> getAllByStatScoreGroupCode(String statScoreGroupCode);
}
