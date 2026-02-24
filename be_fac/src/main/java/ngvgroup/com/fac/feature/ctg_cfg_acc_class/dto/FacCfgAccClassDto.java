package ngvgroup.com.fac.feature.ctg_cfg_acc_class.dto;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.*;
import ngvgroup.com.fac.core.constant.ExcelColumns;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacCfgAccClassDto {

    private Long id;
    @ExcelColumn(ExcelColumns.ACC_CLASS_CODE)
    private String accClassCode;
    @ExcelColumn(ExcelColumns.ACC_CLASS_NAME)
    private String accClassName;

    private String accSideType;

    private String accNature;
    @ExcelColumn(ExcelColumns.DESCRIPTION)
    private String description;

}
