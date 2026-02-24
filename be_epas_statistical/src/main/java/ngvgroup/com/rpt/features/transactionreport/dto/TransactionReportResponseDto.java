package ngvgroup.com.rpt.features.transactionreport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ngvgroup.com.rpt.core.constant.ExcelColumns;

import java.sql.Timestamp;
import java.util.Date;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;

@Data
@AllArgsConstructor
public class TransactionReportResponseDto {
    @ExcelColumn(ExcelColumns.TR_TT)
    private Long id;
    @ExcelColumn(ExcelColumns.TR_STATUS)
    private String currentStatusName;
    private String currentStatusCode;
    @ExcelColumn(ExcelColumns.TR_TXN_DATE)
    private Date txnDate;
    @ExcelColumn(ExcelColumns.TR_STAT_INSTANCE_CODE)
    private String statInstanceCode;
    @ExcelColumn(ExcelColumns.TR_TEMPLATE_CODE)
    private String templateCode;
    private String templateName;
    private String templateGroupCode;
    private String templateGroupName;
    @ExcelColumn(ExcelColumns.TR_FREQUENCY)
    private String commonName;
    @ExcelColumn(ExcelColumns.TR_REPORT_DATA_DATE)
    private Date reportDataDate;
    @ExcelColumn(ExcelColumns.TR_REPORT_DUE_TIME)
    private Timestamp reportDueTime;
    @ExcelColumn(ExcelColumns.TR_CIRCULAR_NAME)
    private String circularName;
    private Integer sendCount;
    private Integer exportCount;
    @ExcelColumn(ExcelColumns.TR_SLA_STATUS)
    private String slaStatus; // Quá hạn / Gần đến hạn / Trong hạn
    private String workflowCode;
    private String reportPeriod;
    private String regulatoryTypeCode ;
    private String circularCode;
    private String orgCode;
    private Timestamp slaDueAt;
    private Integer slaElapsedTime;
    private Integer aggregationRunNo;
}
