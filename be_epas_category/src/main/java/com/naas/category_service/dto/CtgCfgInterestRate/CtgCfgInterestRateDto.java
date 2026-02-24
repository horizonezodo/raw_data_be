package com.naas.category_service.dto.CtgCfgInterestRate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

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
    private Boolean isActive;
    private Boolean isByBalance;

}
