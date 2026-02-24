package ngvgroup.com.fac.feature.fac_inf_acc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacInfAccDetailDto {
    private String orgCode;
    private String currencyCode;
    private String customerCode;

    private String objectTypeCode;
    private String objectCode;

    private String accType;
    private String accClassCode;
    private String accPurposeCode;
    private String domainCode;
    private String accScope;

    private String accNo;
    private String accName;

    private Date openDate;
    private Date closeDate;

    private String accNature;
    private String accStatus;
    private Integer isPrimaryAccount;

    private BigDecimal bal;
    private BigDecimal balAvailable;
    private BigDecimal balActual;

    private BigDecimal intAcrPeriodAmt;
    private BigDecimal intAcrAmt;
    private BigDecimal intAcrYprevAmt;
    private Date intAcrDate;
}
