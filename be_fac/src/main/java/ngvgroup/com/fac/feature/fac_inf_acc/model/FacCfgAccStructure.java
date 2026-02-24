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
@Table(name = "FAC_CFG_ACC_STRUCTURE")
public class FacCfgAccStructure extends BaseEntity {
    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "ACC_STRUCTURE_CODE", length = 64)
    private String accStructureCode;

    @Column(name = "ACC_STRUCTURE_NAME", length = 256)
    private String accStructureName;

    @Column(name = "ACC_TYPE", length = 64)
    private String accType;

    @Column(name = "TOTAL_LENGTH")
    private Integer totalLength;
}
