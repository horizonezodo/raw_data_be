package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatorytype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ngvgroup.bpm.core.common.excel.ExcelColumn;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgStatRegulatoryTypeDTO {
    private Long id;
    @ExcelColumn("Mã văn bản pháp lý")
    private String regulatoryTypeCode;
    @ExcelColumn("Tên văn bản pháp lý")
    private String regulatoryTypeName;
    private String issuedBy;
    private Date issuedDate;
    private Date effectiveDate;
    @ExcelColumn("Mô tả")
    private String description;
    private String workflowCode;

    public CtgCfgStatRegulatoryTypeDTO(String regulatoryTypeCode, String regulatoryTypeName) {
        this.regulatoryTypeCode = regulatoryTypeCode;
        this.regulatoryTypeName = regulatoryTypeName;
    }
}
