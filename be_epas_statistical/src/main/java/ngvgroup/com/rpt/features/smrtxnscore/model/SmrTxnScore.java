package ngvgroup.com.rpt.features.smrtxnscore.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SMR_TXN_SCORE")
public class SmrTxnScore extends BaseEntity {
    @Column(name = "SCORE_INSTANCE_CODE", length = 128, nullable = false)
    private String scoreInstanceCode;
    @Column(name = "CI_ID", length = 128, nullable = false)
    private String ciId;
    @Column(name = "STAT_SCORE_TYPE_CODE", length = 128, nullable = false)
    private String statScoreTypeCode;
    @Column(name = "STAT_SCORE_TYPE_NAME", length = 256, nullable = false)
    private String statScoreTypeName;
    @Column(name = "WORKFLOW_CODE", length = 64, nullable = false)
    private String workflowCode;
    @Column(name = "VERSION_NO", length = 7, nullable = false)
    private int versionNo;
    @Column(name = "TXN_DATE", nullable = false)
    private Date txnDate;
    @Column(name = "SCORE_PERIOD", length = 32, nullable = false)
    private String scorePeriod;
    @Column(name = "SCORE_DATE", nullable = false)
    private Date scoreDate;
    @Column(name = "MAKER_USER_CODE", length = 64, nullable = false)
    private String makerUserCode;
    @Column(name = "MAKER_USER_NAME", length = 128, nullable = false)
    private String makerUserName;
    @Column(name = "TXN_CONTENT", length = 4000)
    private String txnContent;
    @Column(name = "CURRENT_STATUS_CODE", length = 64, nullable = false)
    private String currentStatusCode;
    @Column(name = "CURRENT_STATUS_NAME", length = 128, nullable = false)
    private String currentStatusName;
    @Column(name = "IS_FINAL", nullable = false)
    private Boolean isFinal;
    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";
    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
    
}
