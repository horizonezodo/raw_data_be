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
@Table(name = "CTG_CFG_SCORING_TYPE")
public class CtgCfgScoringType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "RECORD_STATUS", length = 64, nullable = false)
    private String recordStatus;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;

    @Column(name="SCORING_TYPE_CODE",length = 128)
    private String scoringTypeCode;

    @Column(name="SCORING_TYPE_NAME")
    private String scoringTypeName;

    @Column(name="SQL_DATA_COLLECTION")
    private String sqlDataCollection;

    @Column(name="TEMPLATE_COLLECTION_CODE",length = 128)
    private String templateCollectionCode;

    @Column(name="SQL_CALC_RESULT")
    private String sqlCalcResult;

    @Column(name="TEMPLATE_RESULT_CODE",length = 128)
    private String templateResultCode;


    public CtgCfgScoringType( String scoringTypeCode, String scoringTypeName,String description, String sqlDataCollection, String templateCollectionCode, String sqlCalcResult, String templateResultCode) {

        this.scoringTypeCode = scoringTypeCode;
        this.scoringTypeName = scoringTypeName;
        this.sqlDataCollection = sqlDataCollection;
        this.templateCollectionCode = templateCollectionCode;
        this.sqlCalcResult = sqlCalcResult;
        this.templateResultCode = templateResultCode;

        this.setApprovedBy(getCurrentUsername());
        this.setApprovedDate(getTimestampNow());
        this.setRecordStatus(StatusConstants.APPROVAL);
        this.setIsActive(true);
        this.setDescription(description);

    }
}
