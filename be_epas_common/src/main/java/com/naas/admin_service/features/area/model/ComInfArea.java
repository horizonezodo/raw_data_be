package com.naas.admin_service.features.area.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "COM_INF_AREA")
public class ComInfArea extends BaseEntity {

    @Column(name = "AREA_CODE", nullable = false, length = 128)
    private String areaCode;

    @Column(name = "AREA_NAME", nullable = false, length = 256)
    private String areaName;

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "WARD_CODE", length = 128)
    private String wardCode;

    @Column(name = "AREA_TYPE_CODE", length = 64)
    private String areaTypeCode;

    @Column(name = "AREA_TYPE", length = 32)
    private String areaType;

    @Column(name = "IS_RURAL_AREA")
    private Boolean isRuralArea;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

}
