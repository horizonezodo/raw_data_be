package com.naas.admin_service.features.permission.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "COM_CFG_PERMISSION_MAP")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ComCfgPermissionMap extends BaseEntity {

    @Column(name = "GROUP_NAME")
    private String groupName;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "PERMISSION_CODE", nullable = false, length = 150)
    private String permissionCode;
}
