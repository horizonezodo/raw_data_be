package ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.response;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class FacCfgAcctProcessResDTO {
    private String orgCode;
    @ExcelColumn("Chi nhánh")
    private String orgName;
    private String moduleCode;
    @ExcelColumn("Mã phân hệ")
    private String moduleName;
    @ExcelColumn("Mã nghiệp vụ")
    private String processTypeCode;
    @ExcelColumn("Tên nghiệp vụ")
    private String processTypeName;
    @ExcelColumn("Trạng thái")
    private Integer isActive;
    private String modifiedBy;
    private String acctProcessCode;
}
