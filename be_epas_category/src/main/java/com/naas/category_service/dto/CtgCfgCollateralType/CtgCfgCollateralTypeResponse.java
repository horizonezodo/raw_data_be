package com.naas.category_service.dto.CtgCfgCollateralType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgCollateralTypeResponse {
    private Long id;
    private String collateralTypeCode;
    private String collateralTypeName;
    private String deductionRatioFormat;
    private String guaranteeRatioFormat;
    private String riskCoefficientFormat;
    private String status;
    private Boolean isActive;
    private BigDecimal deductionRatio;
    private BigDecimal guaranteeRatio;
    private BigDecimal riskCoefficient;

    public CtgCfgCollateralTypeResponse(Long id, String collateralTypeCode, String collateralTypeName, BigDecimal deductionRatio, BigDecimal guaranteeRatio, BigDecimal riskCoefficient, String status, Boolean isActive) {
        this.id = id;
        this.collateralTypeCode = collateralTypeCode;
        this.collateralTypeName = collateralTypeName;
        this.deductionRatio = deductionRatio;
        this.guaranteeRatio = guaranteeRatio;
        this.riskCoefficient = riskCoefficient;
        this.status = status;
        this.isActive = isActive;
    }
}
