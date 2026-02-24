package ngvgroup.com.rpt.features.report.service;

import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparam.CtgCfgReportParamDTO;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparam.ReportMiningParamDTO;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparam.ReportParamDto;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparam.ResourceParamDTO;
import ngvgroup.com.rpt.features.report.model.CtgCfgReportParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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