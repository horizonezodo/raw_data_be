package ngvgroup.com.bpmn.dto.CtgCfgReport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgReportDTO {
    private Long id;
    private String reportCode;
    private String reportGroupCode;
    private String reportCodeName;
    private String reportCodeNameEn;
    private String dataSourceType;
    private String reportType;
    private Integer isDisplayHomePage;
    private String templateCode;
    private String reportSource;
    private String reportUrlCheck;
    private Integer roundingDigits;
    private String decimalPlaces;
    private String separator;
    private String decimalSeparator;
    private String decimalFormat;
    private String dataFormatType;
    private String zeroDisplayFormat;
    private String negativeDisplayFormat;
    private String description;
    private BigDecimal sortNumber;
}