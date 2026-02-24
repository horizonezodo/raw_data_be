package ngvgroup.com.rpt.features.ctgcfgstatscorerank.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@Entity
@Table(name = "CTG_CFG_STAT_SCORE_TYPE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgStatScoreType extends BaseEntity {
    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "STAT_SCORE_TYPE_CODE", nullable = false, length = 128)
    private String statScoreTypeCode;

    @Column(name = "STAT_SCORE_TYPE_NAME", nullable = false, length = 256)
    private String statScoreTypeName;

    @Column(name = "SQL_CALC_RESULT", length = 4000)
    private String sqlCalcResult;

    @Column(name = "TEMPLATE_RESULT_CODE", length = 128)
    private String templateResultCode;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
