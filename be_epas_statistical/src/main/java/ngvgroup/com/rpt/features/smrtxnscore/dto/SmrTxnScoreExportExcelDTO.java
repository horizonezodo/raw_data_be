package ngvgroup.com.rpt.features.smrtxnscore.dto;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ngvgroup.com.rpt.core.constant.ExcelColumns;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmrTxnScoreExportExcelDTO {

    private Long id;

    @ExcelColumn(ExcelColumns.STS_SCORE_INSTANCE_CODE)
    private String scoreInstanceCode;

    private String ciId;

    private Date txnDate;

    @ExcelColumn(ExcelColumns.STS_MAKER_USER_NAME)
    private String makerUserName;

    @ExcelColumn(ExcelColumns.STS_CURRENT_STATUS_NAME)
    private String currentStatusName;

    @ExcelColumn(ExcelColumns.STS_CI_NAME)
    private String ciName;

    private String ciCode;

    private String scorePeriod;

    private String statScoreTypeName;

    @ExcelColumn(ExcelColumns.STS_SCORE_DATE)
    private Date scoreDate;
}
