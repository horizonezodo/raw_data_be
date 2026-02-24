package com.ngv.aia_service.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiaToolDefineResponse {
    private Long toolId;
    private String actionName;
    private String displayName;
    private String categoryCode;
    private String toolType;
    private String toolDescription;
    private String preconditions;
    private String userParamSchema;
    private String outputSchema;
    private String chainingHints;
    private String errorHandlingHints;
    private String usageExamples;
    private String backendActionCode;
    private String systemParam;
    private String version;
    private Integer isActive;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime modifiedDate;
    private String modifiedBy;
}
