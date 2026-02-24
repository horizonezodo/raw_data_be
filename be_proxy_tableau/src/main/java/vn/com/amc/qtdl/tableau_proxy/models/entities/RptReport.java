package vn.com.amc.qtdl.tableau_proxy.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "RPT_REPORT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RptReport extends BaseEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "SEQ_RPT_REPORT", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEQ_RPT_REPORT", sequenceName = "SEQ_RPT_REPORT", allocationSize = 1)
    private Long id;

    @Column(name = "REPORT_CODE", nullable = false, length = 128)
    private String reportCode;

    @Column(name = "REPORT_GROUP_CODE", nullable = true, length = 128)
    private String reportGroupCode;

    @Column(name = "REPORT_NAME", nullable = true, length = 256)
    private String reportName;

    @Column(name = "REPORT_NAME_EN", nullable = false, length = 256)
    private String reportNameEn;

    @Column(name = "REPORT_TYPE", nullable = false, length = 16)
    private String reportType;

    @Column(name = "DATA_SOURCE_TYPE", nullable = true, length = 64)
    private String dataSourceType;

    @Column(name = "BI_SERVER", nullable = true, length = 64)
    private String biServer;

    @Column(name = "IS_DISPLAY_HOME_PAGE", nullable = true)
    private Integer isDisplayHomePage;

    @Column(name = "REPORT_SOURCE", nullable = true, length = 4000)
    private String reportSource;

    @Column(name = "REPORT_URL", nullable = true, length = 512)
    private String reportUrl;

    @Column(name = "TEMPLATE_FILE_NAME", nullable = true, length = 128)
    private String templateFileName;

    @Column(name = "TEMPLATE_FILE_URL", nullable = true, length = 256)
    private String templateFileUrl;

    @Column(name = "ROUNDING_DIGITS", nullable = true, precision = 3)
    private Integer roundingDigits;

    @Column(name = "DECIMAL_PLACES", nullable = true, precision = 8)
    private Integer decimalPlaces;

    @Column(name = "SEPARATOR", nullable = true, length = 16)
    private String separator;

    @Column(name = "DECIMAL_SEPARATOR", nullable = true, length = 16)
    private String decimalSeparator;

    @Column(name = "DECIMAL_FORMAT", nullable = true, length = 16)
    private String decimalFormat;

    @Column(name = "DATA_FORMAT_TYPE", nullable = true, length = 16)
    private String dataFormatType;

    @Column(name = "ZERO_DISPLAY_FORMAT", nullable = true, length = 16)
    private String zeroDisplayFormat;

    @Column(name = "NEGATIVE_DISPLAY_FORMAT", nullable = true, length = 16)
    private String negativeDisplayFormat;
}