package ngvgroup.com.rpt.features.ctgcfgstat.service;

import ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatcodemap.CtgCfgStatCodeMapDto;
import ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatcodemap.StatCodeMapDto;
import ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatcodemap.StatCodeMappingResponse;
import ngvgroup.com.rpt.features.ctgcfgstat.model.CtgCfgStatCodeMap;

import java.util.List;

public interface CtgCfgStatCodeMapService {
    List<StatCodeMappingResponse> queryInternalStatByTypeCode(String statTypeCode);

    List<StatCodeMapDto> getDataMapping(String statTypeCode, List<String> internalCodes);


    List<CtgCfgStatCodeMap> saveCodeMapping(CtgCfgStatCodeMapDto dto);

    void deleteStatCodeMapByStatTypeCode(String statTypeCode, String statRegulatoryCode);

}
