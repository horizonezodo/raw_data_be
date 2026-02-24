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
@Table(name = "FAC_CFG_ACC_PURPOSE")
public class FacCfgAccPurpose extends BaseEntity {
    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "ACC_PURPOSE_CODE", nullable = false, length = 128)
    private String accPurposeCode;

    @Column(name = "ACC_PURPOSE_NAME", length = 256)
    private String accPurposeName;
}
