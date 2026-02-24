package ngvgroup.com.bpmn.dto.CtgCfgReportGroup;

import ngvgroup.com.bpmn.dto.LabelExcelDto;
import ngvgroup.com.bpmn.dto.request.SearchFilterRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
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
