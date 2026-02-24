package ngvgroup.com.rpt.features.transactionreport.dto;

import lombok.Data;

import java.util.List;

@Data
public class TransactionTransitionRequestDto {

    // Danh sách ID bản ghi RPT_TXN_STAT_TEMPLATE user tích chọn trên lưới
    private List<Long> ids;

    // Mã TRANSITION_CODE user chọn từ droplist
    private String transitionCode;

    // Nội dung comment nhập ở popup Xác nhận
    private String transitionComment;

    private List<KpiAuditRequestDto> kpiAudits;
}