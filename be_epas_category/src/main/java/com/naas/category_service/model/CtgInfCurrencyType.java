package com.naas.category_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "CTG_INF_CURRENCY_TYPE")
public class CtgInfCurrencyType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "RECORD_STATUS", length = 64, nullable = false)
    private String recordStatus;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;

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
}
