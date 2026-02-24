package ngvgroup.com.bpmn.dto.CtgCfgReport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    private Long id;
    private String reportCode;
    private String reportGroupCode;
    private String reportCodeName;
    private String reportGroupName;
    private String dataSourceType;
    private String templateCode;
    private String reportSource;
    private BigInteger sortNumber;
    private BigInteger groupSortNumber;

    public ReportDto(Long id, String reportCode, String reportGroupCode, String reportCodeName, String reportGroupName, String dataSourceType, String templateCode, String reportSource, BigInteger sortNumber) {
        this.id = id;
        this.reportCode = reportCode;
        this.reportGroupCode = reportGroupCode;
        this.reportCodeName = reportCodeName;
        this.reportGroupName = reportGroupName;
        this.dataSourceType = dataSourceType;
        this.templateCode = templateCode;
        this.reportSource = reportSource;
        this.sortNumber = sortNumber;
    }
}
