package com.naas.admin_service.features.users.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "COM_CFG_RESOURCE_MAPPING")
public class CtgCfgResourceMapping extends BaseEntity {
    @Column(name = "RESOURCE_TYPE_CODE", nullable = false, length = 64)
    private String resourceTypeCode;

    @Column(name = "USER_ID", length = 64)
    private String userId;

    @Column(name = "GROUP_ID", length = 32)
    private String groupId;

    @Column(name = "RESOURCE_CODE", nullable = false, length = 128)
    private String resourceCode;

    @Column(name = "RESOURCE_NAME", nullable = false, length = 256)
    private String resourceName;

    @Column(name = "RESOURCE_DESC", length = 256)
    private String resourceDesc;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

    @Column(name = "EFFECTIVE_DATE")
    @Temporal(TemporalType.DATE)
    private Date effectiveDate;

    @Column(name = "EXPIRY_DATE")
    @Temporal(TemporalType.DATE)
    private Date expiryDate;
}
