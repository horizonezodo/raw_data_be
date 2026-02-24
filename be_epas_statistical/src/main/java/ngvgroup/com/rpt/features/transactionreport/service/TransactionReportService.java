package ngvgroup.com.rpt.features.transactionreport.service;

import ngvgroup.com.rpt.features.smrtxnscore.dto.NextStepDto;
import ngvgroup.com.rpt.features.transactionreport.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionReportService {

    /**
     * Tổng hợp dữ liệu báo cáo (theo danh sách template người dùng chọn)
     */
    List<AggregateReportResponseDto> aggregateReport(TransactionReportRequestDto request , String type);

    /**
     * Lưu thông tin chỉnh sửa KPI (tab Thông tin điều chỉnh)
     */
    void saveKpiAudit(TransactionTransitionRequestDto request);

    Page<TransactionReportResponseDto> searchReports(String search, TransactionReportFilterDto filter, Pageable pageable);

    List<NextStepDto> getListNextStep(List<Long> codes);

    void doTransition(TransactionTransitionRequestDto req);

    Page<TransactionReportResultResponseDto> searchReportResults(String keyword,
            TransactionReportFilterDto filter, Pageable pageable);

    boolean existsByStatusCode(String statusCode, String regulatoryTypeCode);
}
