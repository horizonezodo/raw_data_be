package com.naas.category_service.dto.CtgCfgCollateralType;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CtgCfgCollateralTypeDto {
    private String collateralTypeCode;
    private String collateralGroupCode;
    private String collateralTypeName;
    private String orgCode;
    private BigDecimal deductionRatio;
    private BigDecimal guaranteeRatio;
    private BigDecimal riskCoefficient;
    private Boolean isAccountingValue;
    private Boolean isAccountingQuantity;
    private String description;
    private Boolean isActive;

}
