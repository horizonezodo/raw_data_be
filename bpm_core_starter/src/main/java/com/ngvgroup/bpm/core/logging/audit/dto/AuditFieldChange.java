package com.ngvgroup.bpm.core.logging.audit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor       // 👈 bắt buộc cho Jackson
@AllArgsConstructor
public class AuditFieldChange {
    private String fieldName;
    private String oldValue;
    private String newValue;
}