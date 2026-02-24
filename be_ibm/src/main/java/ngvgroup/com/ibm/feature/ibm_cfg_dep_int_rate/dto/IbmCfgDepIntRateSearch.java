package ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.dto;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IbmCfgDepIntRateSearch {
    private Long id;
    @ExcelColumn("Mã lãi suất")
    private String interestRateCode;
    @ExcelColumn("Tên lãi suất")
    private String interestRateName;
    @ExcelColumn("Loại lãi suất")
    private String interestRateType;
    @ExcelColumn("Loại tiền tệ")
    private String currencyCode;
    private Integer isActive;
    @ExcelColumn("Trạng thái")
    private String active;
}
