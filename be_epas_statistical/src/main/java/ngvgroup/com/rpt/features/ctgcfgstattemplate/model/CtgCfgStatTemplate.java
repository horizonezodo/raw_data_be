package ngvgroup.com.rpt.features.ctgcfgstattemplate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CTG_CFG_STAT_TEMPLATE")
public class CtgCfgStatTemplate extends BaseEntity {
    @Column(name = "ORG_CODE",length = 64, nullable = false)
    private String orgCode;
    @Column(name = "TEMPLATE_CODE",length = 128, nullable = false)
    private String templateCode;
    @Column(name = "TEMPLATE_NAME",length = 256, nullable = false)
    private String templateName;
    @Column(name = "TEMPLATE_GROUP_CODE",length = 32, nullable = false)
    private String templateGroupCode;

    @Column(name = "EFFECTIVE_DATE")
    private Date effectiveDate;
    @Column(name = "EXPIRY_DATE")
    private Date expiryDate;
    @Column(name = "COLUMN_START",length = 3)
    private int columnStart;
    @Column(name = "FREQUENCY",length = 64)
    private String frequency;
    @Lob
    @Column(name = "TEMPLATE_FILE", columnDefinition = "BLOB")
    private byte[] templateFile;
    @Lob
    @Column(name = "TEMPLATE_REPORT_FILE", columnDefinition = "BLOB")
    private byte[] templateReportFile;
    @Lob
    @Column(name = "USER_GUIDE_FILE", columnDefinition = "BLOB")
    private byte[] userGuideFile;
    @Column(name = "EXPRESSION_SQL",length = 4000)
    private String expressionSql;
    @Column(name = "TEMPLATE_DATA_TYPE",length = 128)
    private String templateDataType;
    @Column(name = "GENERATED_DATA_TYPE",length = 128)
    private String generateDataType;
    @Column(name = "UNIT_KPI",length = 5)
    private double unitKpi;
    @Column(name = "DATA_FORMAT_TYPE",length = 16)
    private String dataFormatType;
    @Column(name = "DECIMAL_SCALE",length = 1)
    private double decimalScale;
    @Column(name = "DECIMAL_FORMAT",length = 16)
    private String decimalFormat;
    @Column(name = "ROUNDING_DIGITS",length = 3)
    private double roundingDigits;
    @Column(name = "SEPARATOR",length = 16)
    private String separator;
    @Column(name = "DECIMAL_SEPARATOR")
    private String decimalSeparator;
    @Column(name = "ZERO_DISPLAY_FORMAT",length = 16)
    private String zeroDisplayFormat;
    @Column(name = "NEGATIVE_DISPLAY_FORMAT",length = 16)
    private String negativeDisplayFormat;
    @Column(name = "TEMPLATE_FILE_NAME", length = 256)
    private String templateFileName;
    @Column(name = "TEMPLATE_REPORT_FILE_NAME", length = 256)
    private String templateReportFileName;
    @Column(name = "USER_GUIDE_FILE_NAME", length = 256)
    private String userGuideFileName;
    @Column(name = "CIRCULAR_CODE", length = 128, nullable = false)
    private String circularCode;
    @Column(name = "CIRCULAR_NAME", length = 256, nullable = false)
    private String circularName;
    @Column(name = "TEMPLATE_GROUP_NAME",length = 256, nullable = false)
    private String templateGroupName;

    @Column(name = "REGULATORY_TYPE_CODE", length = 64, nullable = false)
    private String regulatoryTypeCode;
    @Column(name = "REGULATORY_TYPE_NAME", length = 128, nullable = false)
    private String regulatoryTypeName;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";
    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
