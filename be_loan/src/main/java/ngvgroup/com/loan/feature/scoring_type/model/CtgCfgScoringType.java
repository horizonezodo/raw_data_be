package ngvgroup.com.loan.feature.scoring_type.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CTG_CFG_SCORING_TYPE")
public class CtgCfgScoringType extends BaseEntity {

    @Column(name = "SCORING_TYPE_CODE", length = 128)
    private String scoringTypeCode;
    @Column(name = "SCORING_TYPE_NAME", length = 256)
    private String scoringTypeName;
    @Column(name = "SQL_DATA_COLLECTION", length = 256)
    private String sqlDataCollection;
    @Column(name = "TEMPLATE_COLLECTION_CODE", length = 128)
    private String templateCollectionCode;
    @Column(name = "SQL_CALC_RESULT", length = 256)
    private String sqlCalcResult;
    @Column(name = "TEMPLATE_RESULT_CODE", length = 128)
    private String templateResultCode;
    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";
    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

    public CtgCfgScoringType(String scoringTypeCode, String scoringTypeName, String description, String sqlDataCollection, String templateCollectionCode, String sqlCalcResult, String templateResultCode) {

        this.scoringTypeCode = scoringTypeCode;
        this.scoringTypeName = scoringTypeName;
        this.sqlDataCollection = sqlDataCollection;
        this.templateCollectionCode = templateCollectionCode;
        this.sqlCalcResult = sqlCalcResult;
        this.templateResultCode = templateResultCode;

        this.setIsActive(1);
        this.setDescription(description);

    }
}
