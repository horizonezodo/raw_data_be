package com.naas.admin_service.features.category.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "COM_INF_CURRENCY_TYPE")
public class CtgInfCurrencyType extends BaseEntity {
    @Column(name="CURRENCY_CODE",length = 64)
    private String currencyCode;

    @Column(name="CURRENCY_NAME",length = 128)
    private String currencyName;

    @Column(name="SYMBOL",length = 32)
    private String symbol;

    @Column(name="COUNTRY")
    private String country;

    @Column(name="DECIMAL_PLACES",precision = 7,scale = 4)
    private BigDecimal decimalPlaces;

   @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
