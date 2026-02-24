package ngvgroup.com.fac.feature.fac_inf_acc.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "FAC_INF_ACC_BAL_A")
public class FacInfAccBalA extends BaseEntity {
    @Column(name = "DATA_TIME")
    private Timestamp dataTime;

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "OBJECT_TYPE_CODE", nullable = false, length = 64)
    private String objectTypeCode;

    @Column(name = "OBJECT_CODE", nullable = false, length = 64)
    private String objectCode;

    @Column(name = "ACC_NO", length = 128)
    private String accNo;

    @Column(name = "BAL_TYPE_CODE")
    private Long balTypeCode;

    @Column(name = "BAL")
    private BigDecimal bal;

    @Column(name = "BAL_AVAILABLE")
    private BigDecimal balAvailable;

    @Column(name = "BAL_ACTUAL")
    private BigDecimal balActual;

    @Column(name = "TOTAL_DR_AMT")
    private BigDecimal totalDrAmt;

    @Column(name = "TOTAL_CR_AMT")
    private BigDecimal totalCrAmt;

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private Integer isActive = 1;

    public static FacInfAccBalA fromBal(FacInfAccBal bal) {
        FacInfAccBalA a = new FacInfAccBalA();
        a.setDataTime(Timestamp.from(Instant.now()));
        a.setOrgCode(bal.getOrgCode());
        a.setObjectTypeCode(bal.getObjectTypeCode());
        a.setObjectCode(bal.getObjectCode());
        a.setAccNo(bal.getAccNo());
        a.setBalTypeCode(bal.getBalTypeCode());
        a.setBal(bal.getBal());
        a.setBalAvailable(bal.getBalAvailable());
        a.setBalActual(bal.getBalActual());
        a.setTotalDrAmt(bal.getTotalDrAmt());
        a.setTotalCrAmt(bal.getTotalCrAmt());
        return a;
    }
}
