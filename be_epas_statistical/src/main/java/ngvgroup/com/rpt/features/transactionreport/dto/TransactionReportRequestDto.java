package ngvgroup.com.rpt.features.transactionreport.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TransactionReportRequestDto {

    // --- Thông tin điều khiển chung ---
    private String orgCode;            // COM_INF_ORGANIZATION.ORG_CODE
    private String regulatoryTypeCode; // CTG_CFG_STAT_TEMPLATE.REGULATORY_TYPE_CODE
    private String reportPeriod;       // CTG_CFG_STAT_COMMON.COMMON_CODE (CM003)
    private Integer year;              // Năm người dùng chọn
    private Integer month;             // Tháng người dùng chọn (nếu có)
    private Integer day;               // Ngày người dùng chọn (nếu có)
    private String precious;           // Quý
    private Date reportDataDate;       // Ngày báo cáo thực tế (được tính tự động)

    private String makerUserCode;      // USER_ENTITY.USER_NAME
    private String makerUserName;      // USER_ENTITY.FIRST_NAME + LAST_NAME
    private String workflowCode;       // Mã workflow loại cơ sở
    private Date txnDate;              // Ngày thực hiện (hiện tại)
    private String currentStatusCode;  // trạng thái hiện tại bản ghi

    // --- Danh sách báo cáo được tích chọn ---
    private List<String> templateCodes;

    // --- Thông tin xác nhận ---
    private String transitionComment;

    private List<String> statInstanceCodes;
}
