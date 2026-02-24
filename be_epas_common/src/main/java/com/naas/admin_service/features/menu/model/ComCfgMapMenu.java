package com.naas.admin_service.features.menu.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "COM_CFG_MAP_MENU")
public class ComCfgMapMenu extends BaseEntity {
    @Column(name = "MENU_ID", nullable = false, length = 128)
    private String menuId;

    @Column(name = "USER_ID", length = 64)
    private String userId;

    @Column(name = "GROUP_NAME", length = 256)
    private String groupName;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
