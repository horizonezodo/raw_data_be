package com.nass.integration_service.model;

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
@Table(name = "COM_CFG_PARAMETER")
public class ComCfgParameter extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus;

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "PARAM_CODE", nullable = false, length = 256)
    private String paramCode;

    @Column(name = "PARAM_NAME", nullable = false, length = 128)
    private String paramName;

    @Column(name = "VALUE_DESCRIPTION", nullable = false, length = 512)
    private String valueDescription;

    @Column(name = "PARAM_DEFAULT_VALUE", nullable = false, length = 128)
    private String paramDefaultValue;

    @Column(name = "PARAM_VALUE", nullable = false, length = 128)
    private String paramValue;

    @Column(name = "PARAM_TYPE", nullable = false, length = 64)
    private String paramType;

    @Column(name = "MODULE_CODE", length = 64)
    private String moduleCode;

    @Column(name = "MODULE_NAME", length = 256)
    private String moduleName;
}
