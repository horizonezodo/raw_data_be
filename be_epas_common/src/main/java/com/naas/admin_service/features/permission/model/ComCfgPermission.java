package com.naas.admin_service.features.permission.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "COM_CFG_PERMISSION")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ComCfgPermission extends BaseEntity {

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

}
