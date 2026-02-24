package com.naas.category_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CTG_INF_TITLE")
@NoArgsConstructor
@AllArgsConstructor
public class CtgInfTitle extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;
    @Column(name = "TITLE_CODE", length = 32, nullable = false)
    private String titleCode;
    @Column(name = "TITLE_NAME", length = 128, nullable = false)
    private String titleName;
    @Column(name = "POSITION_CODE", length = 32, nullable = false)
    private String positionCode;
    @Column(name = "INVESTOR_ROLE", length = 128)
    private String investorRole;
    @Column(name = "EDUCATION_LEVEL", length = 32)
    private String educaionLevel;
    @Column(name = "FUNCTION_TASK", length = 256)
    private String functionTask;
    @Column(name = "RECORD_STATUS", length = 64, nullable = false)
    private String recordStatus;
    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive;
}
