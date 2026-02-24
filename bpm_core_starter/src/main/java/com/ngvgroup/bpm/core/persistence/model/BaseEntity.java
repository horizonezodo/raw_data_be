package com.ngvgroup.bpm.core.persistence.model;

import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity extends BaseEntitySimple {

    @CreatedDate
    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    private Timestamp createdDate;

    @CreatedBy
    @Column(name = "CREATED_BY", nullable = false, updatable = false, length = 64)
    private String createdBy;

    // APPROVE
    @Column(name = "APPROVED_DATE")
    private Timestamp approvedDate;

    @Column(name = "APPROVED_BY", length = 64)
    private String approvedBy;

    // MODIFY
    @LastModifiedDate
    @Column(name = "MODIFIED_DATE")
    private Timestamp modifiedDate;

    @LastModifiedBy
    @Column(name = "MODIFIED_BY", length = 64)
    private String modifiedBy;

    // STATUS
    @Column(name = "IS_DELETE", nullable = false)
    private Integer isDelete = 0;

    @Column(name = "DESCRIPTION", length = 512)
    private String description;

    @PrePersist
    protected void prePersist() {
        if (approvedDate == null) {
            approvedDate = Timestamp.from(Instant.now());
        }
        if (approvedBy == null) {
            approvedBy = (createdBy != null) ? createdBy : "system";
        }
        
    }
}
