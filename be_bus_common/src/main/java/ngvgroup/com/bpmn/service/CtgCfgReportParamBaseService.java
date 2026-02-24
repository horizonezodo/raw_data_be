package ngvgroup.com.bpmn.service;

import ngvgroup.com.bpmn.dto.CtgCfgReportParamBase.CtgCfgReportParamBaseDto;
import ngvgroup.com.bpmn.dto.CtgCfgReportParamBase.CtgCfgReportParamBaseResponse;
import ngvgroup.com.bpmn.dto.request.ExportExcelRequest;
import ngvgroup.com.bpmn.dto.request.SearchFilterRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgReportParamBaseService {
    void createCfgReportParamBase(CtgCfgReportParamBaseDto request);

    void updateCfgReportParamBase(Long id, CtgCfgReportParamBaseDto request);

    void deleteCfgReportParamBase(Long id);

    CtgCfgReportParamBaseDto getDetailCfgReportParamBase(Long id);

    // PageResponse<CtgCfgReportParamBaseResponse>
    // searchCfgReportParamBase(SearchFilterRequest searchFilterRequest);
    List<CtgCfgReportParamBaseResponse> searchCfgReportParamBasev2(SearchFilterRequest searchFilterRequest);

    List<CtgCfgReportParamBaseResponse> getAllCfgReportParamBase();

    ResponseEntity<byte[]> exportExcel(String fileName, ExportExcelRequest request);

    boolean isExistedByParamBaseCode(String paramBaseCode);
}
