package ngvgroup.com.rpt.features.report.dto.ctgcfgreportgroup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.rpt.features.report.dto.LabelExcelDto;
import ngvgroup.com.rpt.features.report.dto.SearchFilterRequest;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgReportGroupDto {

    private String reportGroupCode;
    private String reportGroupName;
    private String description;
    private String nameEng;
    private BigInteger sortNumber;

    public CtgCfgReportGroupDto(String reportGroupCode, String reportGroupName,String nameEng,BigInteger sortNumber) {
        this.reportGroupCode = reportGroupCode;
        this.reportGroupName = reportGroupName;
        this.nameEng = nameEng;
        this.sortNumber = sortNumber;
    }
    public CtgCfgReportGroupDto(String reportGroupCode, String reportGroupName,String nameEng, String description,BigInteger sortNumber) {
        this.reportGroupCode = reportGroupCode;
        this.reportGroupName = reportGroupName;
        this.description = description;
        this.nameEng = nameEng;
        this.sortNumber = sortNumber;
    }
    public CtgCfgReportGroupDto(String reportGroupCode, String reportGroupName) {
        this.reportGroupCode = reportGroupCode;
        this.reportGroupName = reportGroupName;

    }

    private SearchFilterRequest searchFilter;
    private List<LabelExcelDto> labels;


}
