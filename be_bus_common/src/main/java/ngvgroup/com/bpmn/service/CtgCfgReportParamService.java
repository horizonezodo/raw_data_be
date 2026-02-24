package ngvgroup.com.bpmn.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ngvgroup.com.bpmn.dto.CtgCfgReportParam.CtgCfgReportParamDTO;
import ngvgroup.com.bpmn.dto.CtgCfgReportParam.ReportMiningParamDTO;
import ngvgroup.com.bpmn.dto.CtgCfgReportParam.ReportParamDto;
import ngvgroup.com.bpmn.dto.CtgCfgReportParam.ResourceParamDTO;
import ngvgroup.com.bpmn.model.CtgCfgReportParam;

import java.util.List;

public interface CtgCfgReportParamService {
    CtgCfgReportParamDTO create(CtgCfgReportParamDTO reportParam);

    CtgCfgReportParamDTO update(CtgCfgReportParamDTO reportParam);

    List<CtgCfgReportParam> findAll();

    CtgCfgReportParamDTO findByParameterCode(String parameterCode);

    Page<ReportParamDto> searchAllByReportCode(String reportCode, String keyword, Pageable pageable);

    List<ResourceParamDTO> execResourceSql(String parameterCode, String reportCode);

    CtgCfgReportParamDTO findById(Long id);

    List<ReportMiningParamDTO> getAllByReport(String reportCode);

    void deleteById(Long id);

    List<ReportParamDto> getAllResourceParamName(String reportCode);

    List<ReportParamDto> getAllTargetParamNames(String reportCode);

    boolean checkExist(String reportCode,String parameterCode);
}