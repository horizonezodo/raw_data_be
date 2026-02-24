package vn.com.amc.qtdl.tableau_proxy.models.entities;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import vn.com.amc.qtdl.tableau_proxy.enums.RecordStatus;


import jakarta.persistence.*;
import java.util.Date;

/**
 * @Date: 21/09/2023
 * @Author: HungND.Os
 * @Comment: Base entity
 */
@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @CreatedBy
    @Column(name = "CREATED_BY")
    private String createdBy;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;

    @LastModifiedBy
    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    @Column(name = "RECORD_STATUS")
    @Enumerated(EnumType.STRING)
    private RecordStatus recordStatus;

    @Column(name = "IS_DELETE")
    private Boolean isDelete;

    @CreatedBy
    @Column(name = "APPROVED_BY")
    private String approvedBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPROVED_DATE")
    private Date approvedDate;
}

