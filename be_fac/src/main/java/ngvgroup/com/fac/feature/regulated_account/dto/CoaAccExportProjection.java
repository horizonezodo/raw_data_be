package ngvgroup.com.fac.feature.regulated_account.dto;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoaAccExportProjection {
    @ExcelColumn("Mã tài khoản")
    private String accCoaCode;
    @ExcelColumn("Tên tài khoản")
    private String accCoaName;
    @ExcelColumn("Tính chất")
    private String accNature;
}

