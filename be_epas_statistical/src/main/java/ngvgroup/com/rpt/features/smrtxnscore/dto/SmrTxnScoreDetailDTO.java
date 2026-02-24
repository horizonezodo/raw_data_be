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
public class SmrTxnScoreDetailDTO {
    @ExcelColumn(ExcelColumns.STS_SCORE_INSTANCE_CODE)
    private String scoreInstanceCode;

    private Date txnDate;

    private String makerUserName;

    private String statScoreTypeName;

    private String ciName;

    private String ciCode;
}
