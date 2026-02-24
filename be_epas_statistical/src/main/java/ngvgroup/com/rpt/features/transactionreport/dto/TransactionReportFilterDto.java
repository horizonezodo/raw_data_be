package ngvgroup.com.rpt.features.transactionreport.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TransactionReportFilterDto {
    private String orgCode; // Chi nhánh
    private String regulatoryTypeCode; // Loại cơ sở
    private String reportPeriod; // Định kỳ (Ngày/Tháng/Năm)
    private List<String> statusCodes; // Trạng thái (cho phép chọn nhiều)
    private String statInstanceCode; // Mã giao dịch
    private String templateCode; // Mã mẫu biểu
    private Date fromDate; // Từ ngày
    private Date toDate;   // Đến ngày
    private List<String> templateGroupCodes;
    private List<String> circularCodes;
    private List<String> defaultCircularCodes;
    
}
