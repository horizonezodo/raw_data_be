package ngvgroup.com.fac.feature.hrm.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "CRM_INF_CUST_DOC")
public class CrmInfCustDoc extends BaseEntity {
    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "CUSTOMER_CODE", nullable = false, length = 128)
    private String customerCode;

    @Column(name = "IDENTIFICATION_TYPE", length = 32)
    private String identificationType;

    @Column(name = "IDENTIFICATION_ID", length = 128)
    private String identificationId;

    @Column(name = "IDENTIFICATION_ID_OLD", length = 16)
    private String identificationIdOld;

    @Column(name = "ISSUE_DATE")
    private LocalDate issueDate;

    @Column(name = "ISSUE_PLACE", length = 256)
    private String issuePlace;

    @Column(name = "EXPIRY_DATE")
    private LocalDate expiryDate;
}
