package com.naas.admin_service.features.setting.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "COM_CFG_SETTING")
public class ComCfgSetting extends BaseEntity {
    @Column(name = "SETTING_CODE", nullable = false, length = 128)
    private String settingCode;

    @Column(name = "SETTING_VALUE", nullable = false, length = 4000)
    private String settingValue;

    @Column(name = "USER_ID", length = 128)
    private String userId;

    @Column(name = "REFERENCE_CODE", length = 128)
    private String referenceCode;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
