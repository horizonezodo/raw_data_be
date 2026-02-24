package com.ngv.aia_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiaToolDefineRequest {
    
    @NotBlank(message = "Action name is required")
    @Size(max = 64, message = "Action name must not exceed 64 characters")
    private String actionName;

    @NotBlank(message = "Display name is required")
    @Size(max = 255, message = "Display name must not exceed 255 characters")
    private String displayName;

    @NotBlank(message = "Category code is required")
    @Size(max = 32, message = "Category code must not exceed 32 characters")
    private String categoryCode;

    @Size(max = 16, message = "Tool type must not exceed 16 characters")
    private String toolType;

    @NotBlank(message = "Tool description is required")
    @Size(max = 1024, message = "Tool description must not exceed 1024 characters")
    private String toolDescription;

    @Size(max = 1024, message = "Preconditions must not exceed 1024 characters")
    private String preconditions;

    @Size(max = 1024, message = "User param schema must not exceed 1024 characters")
    private String userParamSchema;

    @Size(max = 1024, message = "Output schema must not exceed 1024 characters")
    private String outputSchema;

    @Size(max = 1024, message = "Chaining hints must not exceed 1024 characters")
    private String chainingHints;

    @Size(max = 1024, message = "Error handling hints must not exceed 1024 characters")
    private String errorHandlingHints;

    @Size(max = 1024, message = "Usage examples must not exceed 1024 characters")
    private String usageExamples;

    @NotBlank(message = "Backend action code is required")
    @Size(max = 64, message = "Backend action code must not exceed 64 characters")
    private String backendActionCode;

    @Size(max = 1024, message = "System param must not exceed 1024 characters")
    private String systemParam;

    @Size(max = 16, message = "Version must not exceed 16 characters")
    private String version;

    private Integer isActive;

    @Size(max = 50, message = "Created by must not exceed 50 characters")
    private String createdBy;

    @Size(max = 50, message = "Modified by must not exceed 50 characters")
    private String modifiedBy;
}
