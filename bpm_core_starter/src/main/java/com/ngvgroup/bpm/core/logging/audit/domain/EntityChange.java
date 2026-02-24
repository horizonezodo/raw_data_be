package com.ngvgroup.bpm.core.logging.audit.domain;

import com.ngvgroup.bpm.core.logging.audit.dto.AuditFieldChange;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EntityChange {
    private String tableName;
    private String entityName;
    private Object entityId;
    private ChangeType changeType;
    private List<AuditFieldChange> fieldChanges = new ArrayList<>();
}
