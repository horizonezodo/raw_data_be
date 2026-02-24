package com.nass.integration_service.model;

import com.nass.integration_service.constant.EntityStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {
    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    private Timestamp createdDate;

    @Column(name = "CREATED_BY", nullable = false, length = 32)
    private String createdBy;

    @Column(name = "APPROVED_DATE", nullable = false)
    private Timestamp approvedDate;

    @Column(name = "APPROVED_BY", nullable = false, length = 32)
    private String approvedBy;

    @Column(name = "MODIFIED_DATE", nullable = false)
    private Timestamp modifiedDate;

    @Column(name = "MODIFIED_BY", nullable = false, length = 32)
    private String modifiedBy;

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus;

    @Column(name = "IS_DELETE", nullable = false)
    private Integer isDelete;

    @Column(name = "IS_ACTIVE")
    private Integer isActive;

    @Column(name = "DESCRIPTION", length = 512)
    private String description;

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString("preferred_username");
        }

        return null;
    }

    public Timestamp getTimestampNow() {
        // return Timestamp.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)); // múi
        // giờ gốc
        return Timestamp.from(ZonedDateTime.now(ZoneId.systemDefault()).toInstant());
    }

    @PrePersist
    public void prePersist() {
        String currentUsername = getCurrentUsername();
        Timestamp currentTimestamp = getTimestampNow();
        this.createdDate = currentTimestamp;
        this.createdBy = currentUsername != null ? currentUsername : "system";
        this.approvedDate = currentTimestamp;
        this.approvedBy = currentUsername != null ? currentUsername : "system";
        this.modifiedDate = currentTimestamp;
        this.modifiedBy = currentUsername != null ? currentUsername : "system";
        this.recordStatus = EntityStatus.RecordStatus.APPROVAL.getValue();
        this.isDelete = EntityStatus.IsDelete.NOT_DELETED.getValue();
        this.isActive = EntityStatus.IsActive.ACTIVE.getValue();
    }

    @PreUpdate
    public void preUpdate() {
        String currentUsername = getCurrentUsername();
        Timestamp currentTimestamp = getTimestampNow();
        this.modifiedDate = currentTimestamp;
        this.modifiedBy = currentUsername != null ? currentUsername : "system";
    }

    @PreRemove
    void preRemove() {
        String currentUsername = getCurrentUsername();
        Timestamp currentTimestamp = getTimestampNow();
        this.modifiedDate = currentTimestamp;
        this.modifiedBy = currentUsername != null ? currentUsername : "system";
        this.isDelete = 1;
    }
}
