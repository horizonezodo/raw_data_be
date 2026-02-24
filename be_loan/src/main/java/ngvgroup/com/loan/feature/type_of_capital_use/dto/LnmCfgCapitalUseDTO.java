package ngvgroup.com.loan.feature.type_of_capital_use.dto;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LnmCfgCapitalUseDTO {
    private Long id;
    @ExcelColumn("Mã loại sử dụng vốn")
    @NotBlank
    private String capitalUseCode;
    @ExcelColumn("Tên loại sử dụng vốn")
    @NotBlank
    private String capitalUseName;
    @ExcelColumn("Mô tả")
    private String description;
    private String orgCode;
    private List<LnmCfgCapitalUseRateDTO> capitalUseRateDTOS;

    public LnmCfgCapitalUseDTO(Long id,String capitalUseCode, String capitalUseName, String description) {
        this.id = id;
        this.capitalUseCode = capitalUseCode;
        this.capitalUseName = capitalUseName;
        this.description = description;
    }
}
