package com.naas.admin_service.features.log.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "COM_INF_LOG_AUDIT")
public class ComInfLogAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUDIT_ID", nullable = false)
    private Long auditId;
    
    @Column(name = "REQUEST_ID", length = 128)
    private String requestId;
    
    @Column(name = "TABLE_NAME", length = 32)
    private String tableName;
    
    @Column(name = "FIELD_NAME", length = 32)
    private String fieldName;

    @Column(name = "RECORD_ID", length = 128)
    private String recordId;
    
    @Column(name = "OLD_VALUE", length = 4000)
    private String oldValue;
    
    @Column(name = "NEW_VALUE", length = 4000)
    private String newValue;

}
