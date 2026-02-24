package ngvgroup.com.rpt.features.transactionreport.service;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatTemplate;
import ngvgroup.com.rpt.features.transactionreport.dto.sub.AdjustmentInformationDto;
import ngvgroup.com.rpt.features.transactionreport.dto.sub.CheckDetailDto;
import ngvgroup.com.rpt.features.transactionreport.dto.sub.KeepTrackActionsDto;
import ngvgroup.com.rpt.features.transactionreport.dto.sub.ReportFileRequestDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TransactionReportSubService {

    Page<CheckDetailDto> getCheckDetail(String statInstanceCode, String search, Pageable pageable);

    List<AdjustmentInformationDto> getAdjustmentInformation(String statInstanceCode, String search);

    Page<KeepTrackActionsDto> getKeepTrackActions(String statInstanceCode, String search, Pageable pageable);

    CtgCfgStatTemplate getFileByTemplateCode(String templateCode);

    Map<String, String> getReportSheetsAsHtml(String templateCode, ReportFileRequestDto requestDto) throws BusinessException;

    ResponseEntity<byte[]> getReportResultExcel(String templateCode, ReportFileRequestDto requestDto);

    List<Map<String,Object>> getKpiDetail(String templateCode, String kpiCode, String orgCode, Date reportDataDate);
}
