package ngvgroup.com.rpt.features.smrscore.dto;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ngvgroup.com.rpt.core.constant.ExcelColumns;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchScoreCommonInfo {
    private Long id;
    @ExcelColumn(ExcelColumns.BR_CI_BR_NAME)
    private String ciBrName;
    @ExcelColumn(ExcelColumns.BR_CI_BR_CODE)
    private String ciBrCode;
    @ExcelColumn(ExcelColumns.BR_ACHIEVED_SCORE)
    private BigDecimal achievedScore;
    @ExcelColumn(ExcelColumns.BR_RANK_VALUE)
    private String rankValue;
    private String scoreInstantCode;
    private String ciId;
    private String ciBrId;
    @ExcelColumn(ExcelColumns.RANK_CONTENT)
    private String rankContent;

}
