package ngvgroup.com.loan.feature.collateral_type.dto;

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
    private Integer isActive;
    private BigDecimal deductionRatio;
    private BigDecimal guaranteeRatio;
    private BigDecimal riskCoefficient;

    public CtgCfgCollateralTypeResponse(Long id, String collateralTypeCode, String collateralTypeName,
                                        BigDecimal deductionRatio, BigDecimal guaranteeRatio, BigDecimal riskCoefficient, Integer isActive) {
        this.id = id;
        this.collateralTypeCode = collateralTypeCode;
        this.collateralTypeName = collateralTypeName;
        this.deductionRatio = deductionRatio;
        this.guaranteeRatio = guaranteeRatio;
        this.riskCoefficient = riskCoefficient;
        this.isActive = isActive;
    }
}
