package ngvgroup.com.rpt.features.dim.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "DIM_PROV_D")
public class DimProvD extends BaseEntity {
    @Column(name = "PROVINCE_CODE", length = 128, nullable = false)
    private String provinceCode;

    @Column(name = "PROVINCE_NAME", length = 128)
    private String provinceName;

    @Column(name = "TAX_CODE", length = 64)
    private String taxCode;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
