package com.naas.category_service.model;

import com.naas.category_service.constant.StatusConstants;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CTG_INF_ECONOMIC_TYPE")
public class CtgInfEconomicType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus;

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "ECONOMIC_TYPE_CODE", nullable = false, length = 64)
    private String economicTypeCode;

    @Column(name = "ECONOMIC_TYPE_NAME", nullable = false, length = 256)
    private String economicTypeName;


    public CtgInfEconomicType(Boolean isActive, String orgCode, String economicTypeCode, String economicTypeName,String description) {
        this.isActive = isActive;
        this.orgCode = orgCode;
        this.economicTypeCode = economicTypeCode;
        this.economicTypeName = economicTypeName;
        this.setDescription(description);
        this.setApprovedBy(getCurrentUsername());
        this.setApprovedDate(getTimestampNow());
        this.setRecordStatus(StatusConstants.APPROVAL);
    }
}
