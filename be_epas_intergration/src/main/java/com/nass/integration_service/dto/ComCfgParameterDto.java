package com.nass.integration_service.dto;

import lombok.Data;

@Data
public class ComCfgParameterDto {
    private String paramCode;
    private String paramName;
    private String paramValue;
    private String valueDescription;
    private String orgCode;
    private String paramType;
    private String moduleCode;
    private String moduleName;
    private String paramDefaultValue;
}
