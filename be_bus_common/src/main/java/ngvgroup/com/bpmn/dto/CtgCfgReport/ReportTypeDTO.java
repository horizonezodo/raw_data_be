package ngvgroup.com.bpmn.dto.CtgCfgReport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportTypeDTO {
    private String commonCode;
    private String commonName;
    private String reportGroupCode;
    private String reportGroupName;
    private String description;
    private BigInteger sortNumber;

    public ReportTypeDTO(String commonCode, String reportGroupCode, String reportGroupName, String description) {
        this.commonCode = commonCode;
        this.reportGroupCode = reportGroupCode;
        this.reportGroupName = reportGroupName;
        this.description = description;
    }

    public ReportTypeDTO(String commonCode, String reportGroupCode, String reportGroupName, String description,
                         BigInteger sortNumber) {
        this.commonCode = commonCode;
        this.reportGroupCode = reportGroupCode;
        this.reportGroupName = reportGroupName;
        this.description = description;
        this.sortNumber = sortNumber;
    }
}
