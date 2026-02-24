package ngvgroup.com.rpt.features.report.service;

import ngvgroup.com.rpt.features.report.dto.ctgcfgreport.*;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparam.ReportParamDto;
import ngvgroup.com.rpt.features.report.model.CtgCfgReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CtgCfgReportService {
    CtgCfgReportDTO create(CtgCfgReportDTO report);

    CtgCfgReportDTO update(CtgCfgReportDTO report);

    List<CtgCfgReport> findAll();

    CtgCfgReportDTO findById(Long id);

    void deleteByReportCode(String reportCode);

    List<ReportMenu> getMenu();

    Page<ReportDtoV1> searchListReport(String reportType, String groupCode, String keyword, Pageable pageable);

    Page<ReportDtoV1> searchListReportByRequest(List<ReportSearchRequest> requests, String keyword, Pageable pageable);

    List<ReportMiningMenu> getMiningMenu();

    CtgCfgReportDTO getReport(String reportCode);

    byte[] generateExcelFile(ReportExcelDTO req, String fileName);

    CtgCfgReportDTO exportUrlReport(ReportParamDto reportParamDto);

    void deleteByIds(List<Long> ids);

    Page<ReportDto> getListReportSetting(String reportType, Pageable pageable);

    boolean checkExist(String code);
}