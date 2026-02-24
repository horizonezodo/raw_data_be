package ngvgroup.com.bpmn.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    private Timestamp createdDate;

    @Column(name = "CREATED_BY",nullable = false, length = 32)
    private String createdBy;

    @Column(name = "APPROVED_DATE", nullable = false)
    private Timestamp approvedDate;

    @Column(name = "APPROVED_BY",nullable = false,length = 32)
    private String approvedBy;

    @Column(name = "MODIFIED_DATE",nullable = false)
    private Timestamp modifiedDate;

    @Column(name = "MODIFIED_BY",nullable = false,length = 32)
    private String modifiedBy;

    @Column(name = "IS_DELETE",nullable = false)
    private int isDelete;

    @Column(name = "DESCRIPTION",  length = 512)
    private String description;

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString("preferred_username");
        }

        return null;
    }

    public Timestamp getTimestampNow() {
        return Timestamp.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
    }

    @PrePersist
    public void prePersist() {
        String currentUsername = getCurrentUsername();
        Timestamp currentTimestamp = getTimestampNow();
        this.createdDate = currentTimestamp;
        this.modifiedDate = currentTimestamp;
        this.approvedDate = currentTimestamp;
        this.createdBy = currentUsername != null ? currentUsername : "system";
        this.modifiedBy = currentUsername != null ? currentUsername : "system";
        this.approvedBy = currentUsername != null ? currentUsername : "system";
        this.isDelete = 0;
    }

    @PreUpdate
    public void preUpdate() {
        String currentUsername = getCurrentUsername();
        this.modifiedDate = Timestamp.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        this.modifiedBy = currentUsername != null ? currentUsername : "system";
    }

}
