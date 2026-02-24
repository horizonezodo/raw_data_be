package ngvgroup.com.bpmn.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ngvgroup.com.bpmn.dto.CtgCfgReport.CtgCfgReportDTO;
import ngvgroup.com.bpmn.dto.CtgCfgReport.ReportDto;
import ngvgroup.com.bpmn.dto.CtgCfgReport.ReportExcelDTO;
import ngvgroup.com.bpmn.dto.CtgCfgReport.ReportJasperDTO;
import ngvgroup.com.bpmn.dto.CtgCfgReport.ReportMenu;
import ngvgroup.com.bpmn.dto.CtgCfgReport.ReportMiningMenu;
import ngvgroup.com.bpmn.dto.CtgCfgReport.ReportSearchRequest;
import ngvgroup.com.bpmn.dto.CtgCfgReportParam.ReportParamDto;
import ngvgroup.com.bpmn.model.CtgCfgReport;

import java.util.List;

public interface CtgCfgReportService {
    CtgCfgReportDTO create(CtgCfgReportDTO report);

    CtgCfgReportDTO update(CtgCfgReportDTO report);

    List<CtgCfgReport> findAll();

    CtgCfgReportDTO findById(Long id);

    void deleteByReportCode(String reportCode);

    List<ReportMenu> getMenu();

    Page<ReportDto> searchListReport(String reportType, String groupCode, String keyword, Pageable pageable);

    Page<ReportDto> searchListReportByRequest(List<ReportSearchRequest> requests, String keyword, Pageable pageable);

    List<ReportMiningMenu> getMiningMenu();

    CtgCfgReportDTO getReport(String reportCode);

    byte[] generateExcelFile(ReportExcelDTO req, String fileName);

    // byte[] generateReport(ReportJasperDTO reportJasperDTO) throws Exception;

    CtgCfgReportDTO exportUrlReport(ReportParamDto reportParamDto);

    void deleteByIds(List<Long> ids);

    Page<ReportDto> getListReportSetting(String reportType, Pageable pageable);

    boolean checkExist(String code);
}