package ngvgroup.com.rpt.features.report.dto.ctgcfgreport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
