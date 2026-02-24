package ngvgroup.com.rpt.features.report.service;


import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparamcond.CtgCfgReportParamCondDTO;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparamcond.ReportParamCondDTO;
import ngvgroup.com.rpt.features.report.model.CtgCfgReportParamCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CtgCfgReportParamCondService {
    CtgCfgReportParamCondDTO create(CtgCfgReportParamCondDTO paramCondDTO);

    CtgCfgReportParamCondDTO update(CtgCfgReportParamCondDTO paramCondDTO);

    List<CtgCfgReportParamCond> findAll();

    CtgCfgReportParamCondDTO findById(Long id);

    void deleteById(Long id);

    Page<ReportParamCondDTO> searchAll(String reportCode, String keyword, Pageable pageable);
}