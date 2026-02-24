package com.naas.category_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CTG_CFG_COLLATERAL_TYPE")
public class CtgCfgCollateralType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "COLLATERAL_TYPE_CODE", nullable = false, length = 256)
    private String collateralTypeCode;

    @Column(name = "COLLATERAL_TYPE_NAME", length = 256)
    private String collateralTypeName;

    @Column(name = "RISK_COEFFICIENT", precision = 7, scale = 4)
    private BigDecimal riskCoefficient;

    @Column(name = "DEDUCTION_RATIO", precision = 7, scale = 4)
    private BigDecimal deductionRatio;

    @Column(name = "GUARANTEE_RATIO", precision = 7, scale = 4)
    private BigDecimal guaranteeRatio;

    @Column(name = "IS_ACCOUNTING_VALUE")
    private Boolean isAccountingValue;

    @Column(name = "IS_ACCOUNTING_QUANTITY")
    private Boolean isAccountingQuantity;

    @Column(name = "COLLATERAL_GROUP_CODE", length = 64)
    private String collateralGroupCode;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus;
}
