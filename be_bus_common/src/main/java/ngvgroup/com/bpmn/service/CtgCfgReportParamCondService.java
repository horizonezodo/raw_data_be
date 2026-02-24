package ngvgroup.com.bpmn.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ngvgroup.com.bpmn.dto.CtgCfgReportParamCond.CtgCfgReportParamCondDTO;
import ngvgroup.com.bpmn.dto.CtgCfgReportParamCond.ReportParamCondDTO;
import ngvgroup.com.bpmn.model.CtgCfgReportParamCond;

import java.util.List;

public interface CtgCfgReportParamCondService {
    CtgCfgReportParamCondDTO create(CtgCfgReportParamCondDTO paramCondDTO);

    CtgCfgReportParamCondDTO update(CtgCfgReportParamCondDTO paramCondDTO);

    List<CtgCfgReportParamCond> findAll();

    CtgCfgReportParamCondDTO findById(Long id);

    void deleteById(Long id);

    Page<ReportParamCondDTO> searchAll(String reportCode, String keyword, Pageable pageable);
    //
    // List<ReportParamCondDTO> getAllByReportCode(String reportCode);
}