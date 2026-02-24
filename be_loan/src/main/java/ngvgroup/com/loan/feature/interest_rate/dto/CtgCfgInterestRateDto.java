package ngvgroup.com.loan.feature.interest_rate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgInterestRateDto {

    private String interestCode;
    private String interestName;
    private String interestType;
    private String currencyCode;
    private String moduleCode;
    private String orgCode;
    private String isActiveFormat;
    private Integer isActive;
    private Boolean isByBalance;

}
