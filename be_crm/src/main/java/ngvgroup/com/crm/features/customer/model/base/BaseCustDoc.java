package ngvgroup.com.crm.features.customer.model.base;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseCustDoc extends BaseEntity {

    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "CUSTOMER_CODE", length = 128, nullable = false)
    private String customerCode;

    @Column(name = "IDENTIFICATION_TYPE", length = 32)
    private String identificationType;

    @Column(name = "IDENTIFICATION_ID", length = 128)
    private String identificationId;

    @Column(name = "IDENTIFICATION_ID_OLD", length = 16)
    private String identificationIdOld;

    @Column(name = "ISSUE_DATE")
    private Date issueDate;

    @Column(name = "ISSUE_PLACE", length = 256)
    private String issuePlace;

    @Column(name = "EXPIRY_DATE")
    private Date expiryDate;
}