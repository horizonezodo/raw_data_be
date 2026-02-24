package com.naas.admin_service.features.setting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComCfgParameterResponse {
    private Long id;
    private String paramCode;
    private String paramName;
    private String paramValue;
    private String valueDescription;
    private String orgName;
    private String orgCode;

    public ComCfgParameterResponse(Long id, String paramCode, String paramName, String paramValue, String valueDescription, String orgCode) {
        this.id = id;
        this.paramCode = paramCode;
        this.paramName = paramName;
        this.paramValue = paramValue;
        this.valueDescription = valueDescription;
        this.orgCode = orgCode;
    }

    public ComCfgParameterResponse(Long id, String paramCode, String paramName, String paramValue, String valueDescription) {
        this.id = id;
        this.paramCode = paramCode;
        this.paramName = paramName;
        this.paramValue = paramValue;
        this.valueDescription = valueDescription;
    }
}
