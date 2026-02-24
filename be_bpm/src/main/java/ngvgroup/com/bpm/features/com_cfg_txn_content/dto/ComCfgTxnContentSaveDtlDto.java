package ngvgroup.com.bpm.features.com_cfg_txn_content.dto;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.bpm.core.contants.ExcelColumns;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComCfgTxnContentSaveDtlDto {

    private Long id;

    @NotBlank
    @ExcelColumn(value = ExcelColumns.CONTENT_DTL_CODE)
    private String contentDtlCode;

    @NotBlank
    private String contentValueType;

    @NotBlank
    @ExcelColumn(value = ExcelColumns.CONTENT_VALUE_TYPE)
    private String contentValueTypeName;

    @NotNull
    @ExcelColumn(value = ExcelColumns.CONTENT_VALUE)
    private String contentValue;

    @NotNull
    @ExcelColumn(value = ExcelColumns.SORT_NUMBER)
    private Integer sortNumber;

    private String formatMask;

    private Integer length;
}
