package com.naas.admin_service.features.common.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

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
@Table(name = "COM_CFG_BUS_MODULE")
public class ComCfgBusModule extends BaseEntity{

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "MODULE_CODE", nullable = false, length = 64)
    private String moduleCode;

    @Column(name = "MODULE_NAME", nullable = false, length = 256)
    private String moduleName;

    @Column(name = "MODULE_TYPE_CODE", length = 64)
    private String moduleTypeCode;

    @Column(name = "MODULE_TYPE_NAME", length = 128)
    private String moduleTypeName;

    @Column(name = "SORT_NUMBER", length = 22)
    private Integer sortNumber;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
    
}
