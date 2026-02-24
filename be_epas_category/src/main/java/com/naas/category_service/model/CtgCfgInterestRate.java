package com.naas.category_service.model;

import com.naas.category_service.constant.StatusConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "CTG_CFG_INTEREST_RATE")
public class CtgCfgInterestRate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "RECORD_STATUS", length = 64, nullable = false)
    private String recordStatus;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;

    @Column(name="ORG_CODE",length = 64,nullable = false)
    private String orgCode;

    @Column(name="INTEREST_CODE",length = 64,nullable = false)
    private String interestCode;

    @Column(name="INTEREST_NAME",nullable = false)
    private String interestName;

    @Column(name="MODULE_CODE",length =64 ,nullable = false)
    private String moduleCode;

    @Column(name="CURRENCY_CODE",length = 64,nullable = false)
    private String currencyCode;

    @Column(name = "IS_BY_BALANCE", nullable = false)
    private Boolean isByBalance;

    @Column(name="INTEREST_TYPE", length = 64, nullable = false)
    private String interestType;


    public CtgCfgInterestRate(String interestCode,String interestName,String orgCode,String moduleCode,String interestType,Boolean isActive,String currencyCode,Boolean isByBalance) {
        this.setApprovedBy(getCurrentUsername());
        this.setApprovedDate(getTimestampNow());
        this.setRecordStatus(StatusConstants.APPROVAL);
        this.interestCode = interestCode;
        this.interestName = interestName;
        this.orgCode = orgCode;
        this.moduleCode = moduleCode;
        this.currencyCode = currencyCode;
        this.isByBalance = isByBalance;
        this.interestType = interestType;
        this.isActive = isActive;
    }
}
