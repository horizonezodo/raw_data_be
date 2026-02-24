package ngvgroup.com.crm.features.customer.model.base;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseCustReln extends BaseEntity {

    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "CUSTOMER_CODE", length = 128, nullable = false)
    private String customerCode;

    @Column(name = "RELATED_CUSTOMER_CODE", length = 128)
    private String relatedCustomerCode;

    @Column(name = "RELATION_CODE", length = 64)
    private String relationCode;

    @Column(name = "RELATION_GROUP_CODE", length = 64)
    private String relationGroupCode;

    @Column(name = "RECIPROCAL_RELATION_CODE", length = 64)
    private String reciprocalRelationCode;

    @Column(name = "RELATION_STATUS", length = 64)
    private String relationStatus;

    @Column(name = "CIF_NUMBER", length = 64)
    private String cifNumber;
}