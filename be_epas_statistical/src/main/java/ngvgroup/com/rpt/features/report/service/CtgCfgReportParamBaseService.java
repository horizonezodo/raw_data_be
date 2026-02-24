package ngvgroup.com.rpt.features.report.service;


import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparambase.CtgCfgReportParamBaseDto;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparambase.CtgCfgReportParamBaseResponse;
import ngvgroup.com.rpt.features.report.dto.ExportExcelRequest;
import ngvgroup.com.rpt.features.report.dto.SearchFilterRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgReportParamBaseService {
    void createCfgReportParamBase(CtgCfgReportParamBaseDto request);

    void updateCfgReportParamBase(Long id, CtgCfgReportParamBaseDto request);

    void deleteCfgReportParamBase(Long id);

    CtgCfgReportParamBaseDto getDetailCfgReportParamBase(Long id);

    List<CtgCfgReportParamBaseResponse> searchCfgReportParamBasev2(SearchFilterRequest searchFilterRequest);

    List<CtgCfgReportParamBaseResponse> getAllCfgReportParamBase();

    ResponseEntity<byte[]> exportExcel(String fileName, ExportExcelRequest request);

    boolean isExistedByParamBaseCode(String paramBaseCode);
}
