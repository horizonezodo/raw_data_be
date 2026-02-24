
package com.naas.category_service.model;
import com.naas.category_service.constant.StatusConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "CTG_CFG_SCORING_INDC_GROUP")
public class CtgCfgScoringIndcGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "RECORD_STATUS", length = 64, nullable = false)
    private String recordStatus;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;

    @Column(name="SORT_NUMBER")
    private BigInteger sortNumber;

    @Column (name="SCORING_TYPE_CODE",nullable = false,length = 128)
    private String scoringTypeCode;

    @Column(name="SCORING_INDC_GROUP_CODE",nullable = false,length = 128)
    private String scoringIndcGroupCode;

    @Column(name="SCORING_INDC_GROUP_NAME")
    private String scoringIndcGroupName;

    @Column(name="SCORING_INDC_GROUP_TYPE",length = 128)
    private String scoringIndcGroupType;

    @Column(name = "WEIGHT_SCORE", precision = 7, scale = 4)
    private BigDecimal weightScore;


    public CtgCfgScoringIndcGroup( String scoringTypeCode, String scoringIndcGroupCode, String scoringIndcGroupName, String scoringIndcGroupType, BigDecimal weightScore, BigInteger sortNumber,String description) {
        this.scoringTypeCode = scoringTypeCode;
        this.scoringIndcGroupCode = scoringIndcGroupCode;
        this.scoringIndcGroupName = scoringIndcGroupName;
        this.scoringIndcGroupType = scoringIndcGroupType;
        this.weightScore = weightScore;
        this.sortNumber = sortNumber;
        this.setApprovedBy(getCurrentUsername());
        this.setApprovedDate(getTimestampNow());
        this.setRecordStatus(StatusConstants.APPROVAL);
        this.setIsActive(true);
        this.setDescription(description);

    }
}

