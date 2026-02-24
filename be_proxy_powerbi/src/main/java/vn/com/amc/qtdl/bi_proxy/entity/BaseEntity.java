package vn.com.amc.qtdl.bi_proxy.entity;

import vn.com.amc.qtdl.bi_proxy.enums.RecordStatus;
import vn.com.amc.qtdl.bi_proxy.utils.RecordStatusConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

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
    @Convert(converter = RecordStatusConverter.class)
    private RecordStatus recordStatus = RecordStatus.APPROVAL;

    @Column(name = "IS_DELETE")
    private Boolean isDelete = false;

    @CreatedBy
    @Column(name = "APPROVED_BY")
    private String approvedBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPROVED_DATE")
    private Date approvedDate;

    @Column(name = "DESCRIPTION",  length = 512)
    private String description;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive = true;

}
