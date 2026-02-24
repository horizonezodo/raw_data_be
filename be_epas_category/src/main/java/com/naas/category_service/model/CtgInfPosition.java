package com.naas.category_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CTG_INF_POSITION")
@NoArgsConstructor
@AllArgsConstructor
public class CtgInfPosition extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;
    @Column(name = "POSITION_CODE", length = 32, nullable = false)
    private String positionCode;
    @Column(name = "POSITION_NAME", length = 128, nullable = false)
    private String positionName;
    @Column(name = "RECORD_STATUS", length = 64, nullable = false)
    private String recordStatus;
    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive;
}
