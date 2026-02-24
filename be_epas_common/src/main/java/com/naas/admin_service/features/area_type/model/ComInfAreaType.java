package com.naas.admin_service.features.area_type.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "COM_INF_AREA_TYPE")
@NoArgsConstructor
@AllArgsConstructor
public class ComInfAreaType extends BaseEntity {

    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "AREA_TYPE_CODE", length = 64)
    private String areaTypeCode;

    @Column(name = "AREA_TYPE_NAME", length = 128)
    private String areaTypeName;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

}
