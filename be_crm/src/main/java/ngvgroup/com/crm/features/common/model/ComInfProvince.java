package ngvgroup.com.crm.features.common.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "COM_INF_PROVINCE")
@NoArgsConstructor
@Setter
@Getter
public class ComInfProvince extends BaseEntity {
    @Column(name = "PROVINCE_CODE", length = 128, nullable = false)
    private String provinceCode;

    @Column(name = "PROVINCE_NAME", length = 128, nullable = false)
    private String provinceName;

    @Column(name = "TAX_CODE", length = 64)
    private String taxCode;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

}
