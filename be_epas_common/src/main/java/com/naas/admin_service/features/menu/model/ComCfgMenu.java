package com.naas.admin_service.features.menu.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "COM_CFG_MENU")
public class ComCfgMenu extends BaseEntity {
    @Column(name = "MENU_ID", nullable = false, length = 128)
    private String menuId;

    @Column(name = "MENU_NAME", nullable = false, length = 256)
    private String menuName;

    @Column(name = "PARENT_ID", length = 128)
    private String parentId;

    @Column(name = "MENU_URL", nullable = false, length = 256)
    private String menuUrl;

    @Column(name = "SORT_NUMBER", length = 22)
    private Integer sortNumber;

    @Column(name = "ICON", length = 32)
    private String icon;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
