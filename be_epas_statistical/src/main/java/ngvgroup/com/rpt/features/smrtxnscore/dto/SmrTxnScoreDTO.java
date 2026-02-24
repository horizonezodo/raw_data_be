package ngvgroup.com.rpt.features.smrtxnscore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;

import ngvgroup.com.rpt.core.constant.ExcelColumns;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmrTxnScoreDTO {
    private Long id;

    @ExcelColumn(ExcelColumns.STS_SCORE_INSTANCE_CODE)
    private String scoreInstanceCode;

    private String ciId;

    private String statScoreTypeCode;

    private String statScoreTypeName;

    private String workflowCode;

    private int versionNo;

    private Date txnDate;

    private String scorePeriod;

    @ExcelColumn(ExcelColumns.STS_SCORE_DATE)
    private Date scoreDate;

    private String makerUserCode;

    @ExcelColumn(ExcelColumns.STS_MAKER_USER_NAME)
    private String makerUserName;

    private String txnContent;

    private String currentStatusCode;

    @ExcelColumn(ExcelColumns.STS_CURRENT_STATUS_NAME)
    private String currentStatusName;

    private Boolean isFinal;

    @ExcelColumn(ExcelColumns.STS_CI_NAME)
    private String ciName;

    private String ciCode;

}
