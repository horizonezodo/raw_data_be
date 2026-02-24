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
@Table(name = "FAC_INF_ACC_SEQ")
public class FacInfAccSeq extends BaseEntity {
    @Column(name = "ACC_STRUCTURE_CODE", nullable = false, length = 64)
    private String accStructureCode;

    @Column(name = "ACC_TYPE", length = 64)
    private String accType;

    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "ACC_DOMAIN", length = 128)
    private String accDomain;

    @Column(name = "CURRENCY_CODE", length = 4)
    private String currencyCode;

    @Column(name = "LAST_SEQ_NO")
    private Integer lastSeqNo;

    @Column(name = "MAX_LENGTH")
    private Integer maxLength;

    @Column(name = "IS_OVERFLOW")
    private Integer isOverflow;

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private Integer isActive = 1;
}
