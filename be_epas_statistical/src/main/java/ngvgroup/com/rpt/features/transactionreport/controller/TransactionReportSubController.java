package ngvgroup.com.rpt.features.transactionreport.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatTemplate;
import ngvgroup.com.rpt.features.transactionreport.common.FileDownloadUtil;
import ngvgroup.com.rpt.features.transactionreport.dto.sub.AdjustmentInformationDto;
import ngvgroup.com.rpt.features.transactionreport.dto.sub.CheckDetailDto;
import ngvgroup.com.rpt.features.transactionreport.dto.sub.KeepTrackActionsDto;
import ngvgroup.com.rpt.features.transactionreport.dto.sub.ReportFileRequestDto;
import ngvgroup.com.rpt.features.transactionreport.service.TransactionReportSubService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transaction-report-sub")
@RequiredArgsConstructor
public class TransactionReportSubController {

    private final TransactionReportSubService service;

    @LogActivity(function = "Lấy chi tiết kiểm tra")
    @GetMapping("/check-detail/{statInstanceCode}")
    public ResponseEntity<ResponseData<Page<CheckDetailDto>>> getCheckDetail(
            @PathVariable String statInstanceCode,
            @RequestParam String search,
            Pageable pageable
    ) {
        return ResponseData.okEntity(service.getCheckDetail(statInstanceCode, search, pageable));
    }

    @LogActivity(function = "Lấy thông tin điều chỉnh")
    @GetMapping("/adjustment-information/{statInstanceCode}")
    public ResponseEntity<ResponseData<List<AdjustmentInformationDto>>> getAdjustmentInformation(
            @PathVariable String statInstanceCode,
            @RequestParam String search
    ) {
        return ResponseData.okEntity(service.getAdjustmentInformation(statInstanceCode, search));
    }

    @LogActivity(function = "Lấy lịch sử thao tác")
    @GetMapping("/keep-track-actions/{statInstanceCode}")
    public ResponseEntity<ResponseData<Page<KeepTrackActionsDto>>> getKeepTrackActions(
            @PathVariable String statInstanceCode,
            @RequestParam String search,
            Pageable pageable
    ) {
        return ResponseData.okEntity(service.getKeepTrackActions(statInstanceCode, search, pageable));
    }

    @LogActivity(function = "Lấy báo cáo dạng HTML")
    @PostMapping("/report-file/{templateCode}")
    public ResponseEntity<Map<String, String>> getReportAsHtml(
            @PathVariable String templateCode,
            @RequestBody ReportFileRequestDto requestDto) {
        try {
            // Truyền templateCode và object requestDto xuống service
            Map<String, String> result = service.getReportSheetsAsHtml(templateCode, requestDto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @LogActivity(function = "Lấy kết quả báo cáo")
    @PostMapping("/report-result-file/{templateCode}")
    public ResponseEntity<byte[]> getReportResult(
            @PathVariable String templateCode,
            @RequestBody ReportFileRequestDto requestDto) {
        try {
            // Truyền templateCode và object requestDto xuống service
            return service.getReportResultExcel(templateCode, requestDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @LogActivity(function = "Lấy hướng dẫn lấy dữ liệu")
    @GetMapping("/how-to-get-data/{templateCode}")
    public ResponseEntity<byte[]> getTemplateGetData(
            @PathVariable String templateCode
    ) {
        CtgCfgStatTemplate template = service.getFileByTemplateCode(templateCode);

        String fileName = template.getUserGuideFileName();
        byte[] fileBytes = template.getUserGuideFile();

        return FileDownloadUtil.buildFileResponse(fileName, fileBytes);
    }

    @LogActivity(function = "Lấy chi tiết KPI báo cáo")
    @GetMapping("/report-kpi-detail")
    public ResponseEntity<ResponseData<List<Map<String,Object>>>> getKpiDetail(
            @RequestParam("templateCode") String templateCode,
            @RequestParam("kpiCode") String kpiCode,
            @RequestParam("orgCode") String orgCode,
            @RequestParam("reportDataDate")
            @DateTimeFormat(pattern = "dd-MM-yyyy") Date reportDataDate) {
        return ResponseData.okEntity(
                service.getKpiDetail(templateCode, kpiCode, orgCode, reportDataDate)
        );
    }
}
