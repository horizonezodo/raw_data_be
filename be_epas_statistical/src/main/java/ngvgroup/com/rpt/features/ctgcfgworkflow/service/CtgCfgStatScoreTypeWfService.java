package ngvgroup.com.rpt.features.ctgcfgworkflow.service;

import ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow.CtgCfgStatScoreTypeWfDto;

public interface CtgCfgStatScoreTypeWfService {
    void create(CtgCfgStatScoreTypeWfDto ctgCfgStatScoreTypeWfDto);
    void update(CtgCfgStatScoreTypeWfDto ctgCfgStatScoreTypeWfDto);
    CtgCfgStatScoreTypeWfDto getDetail(String statScoreTypeCode);
    void delete(String statScoreTypeCode);
}
