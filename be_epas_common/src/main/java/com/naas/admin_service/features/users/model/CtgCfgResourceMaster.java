package com.naas.admin_service.features.users.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COM_CFG_RESOURCE_MASTER")
public class CtgCfgResourceMaster extends BaseEntity {

    @Column(name = "RESOURCE_TYPE_CODE", length = 64, nullable = false)
    private String resourceTypeCode;

    @Column(name = "RESOURCE_TYPE_NAME", length = 64, nullable = false)
    private String resourceTypeName;

    @Column(name = "RESOURCE_SQL", length = 4000, nullable = false)
    private String resourceSql;

    @Column(name = "MODULE_CODE", length = 64)
    private String moduleCode;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
