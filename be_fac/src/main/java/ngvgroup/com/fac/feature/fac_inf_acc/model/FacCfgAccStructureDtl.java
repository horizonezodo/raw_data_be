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
@Table(name = "FAC_CFG_ACC_STRUCTURE_DTL")
public class FacCfgAccStructureDtl  extends BaseEntity {
    @Column(name = "SORT_NUMBER")
    private Long sortNumber;

    @Column(name = "ACC_STRUCTURE_CODE", length = 64)
    private String accStructureCode;

    @Column(name = "SEGMENT_CODE", length = 64)
    private String segmentCode;

    @Column(name = "SEGMENT_TYPE", length = 64)
    private String segmentType;

    @Column(name = "SEGMENT_LENGTH")
    private Integer segmentLength;

    @Column(name = "SEGMENT_SOURCE", length = 256)
    private String segmentSource;

    @Column(name = "SEGMENT_VALUE", length = 512)
    private String segmentValue;

    @Column(name = "GEN_SCOPE", length = 512)
    private String genScope;

    @Column(name = "IS_SEQ")
    private Integer isSeq;

    @Column(name = "IS_REQUIRED")
    private Integer isRequired;
}

