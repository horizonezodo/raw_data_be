package ngvgroup.com.loan.feature.scoring_indc.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CTG_CFG_SCORING_INDC")
public class CtgCfgScoringIndc extends BaseEntity {

    @Column(name = "INDICATOR_CODE", length = 128)
    private String indicatorCode;
    @Column(name = "INDICATOR_NAME", length = 256)
    private String indicatorName;
    @Column(name = "WEIGHT_SCORE", precision = 7, scale = 4)
    private BigDecimal weightScore;
    @Column(name = "SQL_EXECUTE", length = 512)
    private String sqlExecute;
    @Column(name = "CONTROL_TYPE", length = 128)
    private String controlType;
    @Column(name = "DATA_TYPE", length = 4000)
    private String dataType;
    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";
    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;


}
