package ngvgroup.com.crm.features.customer.model.base;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseCustAddr extends BaseEntity {

    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "CUSTOMER_CODE", length = 128, nullable = false)
    private String customerCode;

    @Column(name = "COUNTRY_CODE", length = 128)
    private String countryCode;

    @Column(name = "PROVINCE_CODE", length = 128)
    private String provinceCode;

    @Column(name = "WARD_CODE", length = 128)
    private String wardCode;

    @Column(name = "ADDRESS", length = 512)
    private String address;

    @Column(name = "IS_PRIMARY")
    private Integer isPrimary;
}