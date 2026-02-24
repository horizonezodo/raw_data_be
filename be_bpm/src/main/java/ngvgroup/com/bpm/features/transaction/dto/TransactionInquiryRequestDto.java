package ngvgroup.com.bpm.features.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInquiryRequestDto {
    private String orgCode;               // Chi nhánh
    private Timestamp fromDate;           // Từ ngày
    private Timestamp toDate;             // Đến ngày
    private List<String> processTypeCodes;// Phân loại giao dịch (List)
    private String businessStatus;        // Trạng thái giao dịch (ALL, ACTIVE, COMPLETE, CANCEL)
    private String slaStatus;             // Trạng thái SLA (ALL, WITHIN, APPROACH, OVER)
}