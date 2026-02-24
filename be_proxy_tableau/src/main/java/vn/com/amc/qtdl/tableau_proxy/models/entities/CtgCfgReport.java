package vn.com.amc.qtdl.tableau_proxy.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "CTG_CFG_REPORT")
public class CtgCfgReport extends BaseEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(name = "REPORT_CODE", nullable = false, length = 128)
    private String reportCode;

    @Column(name = "REPORT_GROUP_CODE", length = 128)
    private String reportGroupCode;

    @Column(name = "REPORT_CODE_NAME", length = 256)
    private String reportCodeName;

    @Column(name = "REPORT_CODE_NAME_EN", nullable = false, length = 256)
    private String reportCodeNameEn;

    @Column(name = "DATA_SOURCE_TYPE", length = 64)
    private String dataSourceType;

    @Column(name = "REPORT_TYPE", nullable = false, length = 16)
    private String reportType;

    @Column(name = "IS_DISPLAY_HOME_PAGE")
    private Integer isDisplayHomePage;

    @Column(name = "TEMPLATE_CODE", nullable = false, length = 128)
    private String templateCode;

    @Column(name = "REPORT_SOURCE", nullable = false, length = 4000)
    private String reportSource;

    @Column(name = "REPORT_URL_CHECK", length = 512)
    private String reportUrlCheck;

    @Column(name = "ROUNDING_DIGITS")
    private Integer roundingDigits;

    @Column(name = "DECIMAL_PLACES", precision = 7, scale = 4)
    private String decimalPlaces;

    @Column(name = "SEPARATOR", length = 16)
    private String separator;

    @Column(name = "DECIMAL_SEPARATOR", length = 16)
    private String decimalSeparator;

    @Column(name = "DECIMAL_FORMAT", length = 16)
    private String decimalFormat;

    @Column(name = "DATA_FORMAT_TYPE", length = 16)
    private String dataFormatType;

    @Column(name = "ZERO_DISPLAY_FORMAT", length = 16)
    private String zeroDisplayFormat;

    @Column(name = "NEGATIVE_DISPLAY_FORMAT", length = 16)
    private String negativeDisplayFormat;

    @Column(name="SORT_NUMBER")
    private BigInteger sortNumber;
}
