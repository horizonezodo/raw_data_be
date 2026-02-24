package ngvgroup.com.rpt.features.smrscore.dto;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ngvgroup.com.rpt.core.constant.ExcelColumns;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmrScoreSearchDto {
    private Long id;
    private String ciId;

    @ExcelColumn(ExcelColumns.SSS_CI_NAME)
    private String ciName;
    private String ciCode;
    @ExcelColumn(ExcelColumns.SSS_TXN_DATE)
    private Date txnDate;
    @ExcelColumn(ExcelColumns.SSS_MAKER_USER_NAME)
    private String makerUserName;
    @ExcelColumn(ExcelColumns.SSS_SCORE_INSTANCE_CODE)
    private String scoreInstanceCode;
    private String scorePeriod;
    private String statScoreTypeName;
    private Date scoreDate;
}
