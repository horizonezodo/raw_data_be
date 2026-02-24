package ngvgroup.com.rpt.features.transactionreport.controller;

import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.smrtxnscore.dto.NextStepDto;
import ngvgroup.com.rpt.features.transactionreport.dto.AggregateReportResponseDto;
import ngvgroup.com.rpt.features.transactionreport.dto.TransactionReportFilterDto;
import ngvgroup.com.rpt.features.transactionreport.dto.TransactionReportRequestDto;
import ngvgroup.com.rpt.features.transactionreport.dto.TransactionReportResponseDto;
import ngvgroup.com.rpt.features.transactionreport.dto.TransactionReportResultResponseDto;
import ngvgroup.com.rpt.features.transactionreport.dto.TransactionTransitionRequestDto;
import ngvgroup.com.rpt.features.transactionreport.service.TransactionReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;

import java.util.List;

@RestController
@RequestMapping("/transaction-report")
@RequiredArgsConstructor
public class TransactionReportController {

    private final TransactionReportService service;
    private final ExportExcel exportExcel;

    /**
     * Tổng hợp dữ liệu báo cáo
     */
    @LogActivity(function = "Tổng hợp dữ liệu báo cáo")
    @PostMapping("/aggregate")
    public ResponseEntity<ResponseData<List<AggregateReportResponseDto>>> aggregate(
            @RequestBody TransactionReportRequestDto req,
            @RequestParam String type
    ) {
        List<AggregateReportResponseDto> result = service.aggregateReport(req , type);
        return ResponseData.okEntity(result);
    }

    /**
     * Tìm kiếm báo cáo (tab Lịch sử / Danh sách)
     */
    @LogActivity(function = "Tìm kiếm báo cáo giao dịch")
    @PostMapping("/search")
    public ResponseEntity<ResponseData<Page<TransactionReportResponseDto>>> search(
            @RequestParam(required = false) String keyword,
            @RequestBody TransactionReportFilterDto filter,
            Pageable pageable
    ) {
        Page<TransactionReportResponseDto> result = service.searchReports(keyword, filter, pageable);
        return ResponseData.okEntity(result);
    }

    @LogActivity(function = "Tìm kiếm kết quả báo cáo")
    @PostMapping("/result-search")
    public ResponseEntity<ResponseData<Page<TransactionReportResultResponseDto>>> searchResults(
            @RequestParam(required = false) String keyword,
            @RequestBody TransactionReportFilterDto filter,
            Pageable pageable
    ) {
        Page<TransactionReportResultResponseDto> result = service.searchReportResults(keyword, filter, pageable);
        return ResponseData.okEntity(result);
    }
    @LogActivity(function = "Lấy danh sách bước tiếp theo")
    @PostMapping("/list-next-step")
    public ResponseEntity<ResponseData<List<NextStepDto>>> getListNextStep(
            @RequestBody List<Long> ids
    ) {
        return ResponseData.okEntity(service.getListNextStep(ids));
    }

    @LogActivity(function = "Xuất Excel báo cáo giao dịch")
    @PostMapping("/export-to-excel")
    public ResponseEntity<byte[]> exportToExcel(
            @RequestParam(required = false) String search,
            @RequestBody TransactionReportFilterDto filter,
            @RequestParam(required = false) String exportType,
            Pageable pageable) throws Exception {
        if("all".equalsIgnoreCase(exportType)) {
            pageable = Pageable.unpaged();
        }
        List<TransactionReportResponseDto> result = service.searchReports(search, filter, pageable).getContent();
        return exportExcel.exportExcel(result, "Transaction_Report.xlsx");
    }

    @LogActivity(function = "Xuất Excel kết quả báo cáo")
    @PostMapping("/export-result-to-excel")
    public ResponseEntity<byte[]> exportResultToExcel(
            @RequestParam(required = false) String search,
            @RequestBody TransactionReportFilterDto filter,
            @RequestParam(required = false) String exportType,
            Pageable pageable) throws Exception {
        if("all".equalsIgnoreCase(exportType)) {
            pageable = Pageable.unpaged();
        }
        List<TransactionReportResultResponseDto> result = service.searchReportResults(search, filter, pageable).getContent();
        return exportExcel.exportExcel(result, "Transaction_Report_Result.xlsx");
    }

    @LogActivity(function = "Thực hiện chuyển trạng thái")
    @PostMapping("/transition")
    public ResponseEntity<ResponseData<String>> doTransition(@RequestBody TransactionTransitionRequestDto request) {
        service.saveKpiAudit(request);
        service.doTransition(request);
        return ResponseData.okEntity("transition success");
    }

    @LogActivity(function = "Kiểm tra giao dịch hợp lệ")
    @GetMapping("/validate-transaction")
    public ResponseEntity<ResponseData<Boolean>> validateTransaction(
        @RequestParam(name = "statusCode", required = false) String statusCode,
        @RequestParam(name = "regulatoryTypeCode", required = false) String regulatoryTypeCode
    ) {
        return ResponseData.okEntity(service.existsByStatusCode(statusCode, regulatoryTypeCode));
    }

}
