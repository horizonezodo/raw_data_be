package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgStatTemplateDto {
    private Long id;
    private String orgCode;
    private String templateCode;
    private String templateName;
    private String templateGroupCode;
    private Date effectiveDate;
    private Date expiryDate;
    private int columnStart;
    private String frequency;
    private byte[] templateFile;
    private byte[] templateReportFile;
    private byte[] userGuideFile;
    private String expressionSql;
    private String templateDataType;
    private String generateDataType;
    private double unitKpi;
    private String dataFormatType;
    private double decimalScale;
    private String decimalFormat;
    private double roundingDigits;
    private String separator;
    private String decimalSeparator;
    private String zeroDisplayFormat;
    private String negativeDisplayFormat;
    private String description;
    private String templateGroupName;
    private String templateFileName;
    private String templateReportFileName;
    private String userGuideFileName;
    private String circularCode;
    private String circularName;
    private String regulatoryTypeCode;
    private String regulatoryTypeName;
    private String commonName;
}
