package ngvgroup.com.crm.features.customer.model.base;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseCustLink extends BaseEntity {

    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "CUSTOMER_CODE", length = 128, nullable = false)
    private String customerCode;

    @Column(name = "SOURCE_CODE", length = 32)
    private String sourceCode;

    @Column(name = "SOURCE_CUSTOMER_ID", length = 128)
    private String sourceCustId;

    @Column(name = "LINK_STATUS", length = 64)
    private String linkStatus;

    @Column(name = "EXTERNAL_REF_CODE", length = 128)
    private String externalRefCode;
}