package ngvgroup.com.crm.features.customer.model.base;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseCustSeg extends BaseEntity {

    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "CUSTOMER_CODE", length = 128, nullable = false)
    private String customerCode;

    @Column(name = "SEGMENT_TYPE", length = 64)
    private String segmentType;

    @Column(name = "SEGMENT_CODE", length = 64)
    private String segmentCode;

    @Column(name = "SEGMENT_RANK_CODE", length = 32)
    private String segmentRankCode;
}