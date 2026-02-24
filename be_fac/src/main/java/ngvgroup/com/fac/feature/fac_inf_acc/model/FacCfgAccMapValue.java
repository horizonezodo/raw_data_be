package ngvgroup.com.fac.feature.fac_inf_acc.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "FAC_CFG_ACC_MAP_VALUE")
public class FacCfgAccMapValue  extends BaseEntity {
    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "MAP_CODE", length = 128)
    private String mapCode;

    @Column(name = "BUSINESS_CODE", length = 128)
    private String businessCode;

    @Column(name = "MAP_VALUE", length = 256)
    private String mapValue;
}
