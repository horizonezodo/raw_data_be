package com.ngv.aia_service.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "AIA_TOOL_DEFINE")
@Getter
@Setter
@ToString
public class AiaToolDefine implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TOOL_ID")
    private Long toolId;

    @Column(name = "ACTION_NAME", length = 64, nullable = false)
    private String actionName;

    @Column(name = "DISPLAY_NAME", nullable = false)
    private String displayName;

    @Column(name = "CATEGORY_CODE", length = 32, nullable = false)
    private String categoryCode;

    @Column(name = "TOOL_TYPE", length = 16, nullable = false)
    private String toolType = "API_CALL";

    @Column(name = "TOOL_DESCRIPTION", nullable = false)
    private String toolDescription;

    @Column(name = "PRECONDITIONS")
    private String preconditions;

    @Column(name = "USER_PARAM_SCHEMA")
    private String userParamSchema;

    @Column(name = "OUTPUT_SCHEMA")
    private String outputSchema;

    @Column(name = "CHAINING_HINTS")
    private String chainingHints;

    @Column(name = "ERROR_HANDLING_HINTS")
    private String errorHandlingHints;

    @Column(name = "USAGE_EXAMPLES")
    private String usageExamples;

    @Column(name = "BACKEND_ACTION_CODE", length = 64, nullable = false)
    private String backendActionCode;

    @Column(name = "SYSTEM_PARAM")
    private String systemParam;

    @Column(name = "VERSION", length = 16, nullable = false)
    private String version = "1.0";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

    @Column(name = "CREATED_DATE", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;

    @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDate;

    @Column(name = "MODIFIED_BY", length = 50)
    private String modifiedBy;

    @PrePersist
    protected void onCreate() {
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = LocalDateTime.now();
    }
}
