package ngvgroup.com.rpt.features.smrtxnscore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SMR_TXN_SCORE_STATUS")
public class SmrTxnScoreStatus extends BaseEntity {
    @Column(name = "SCORE_INSTANCE_CODE", length = 128, nullable = false)
    private String scoreInstanceCode;
    @Column(name = "TXN_USER_ID", length = 128, nullable = false)
    private String txnUserId;
    @Column(name = "TXN_USER_NAME", length = 256, nullable = false)
    private String txnUserName;
    @Column(name = "TRANSITION_COMMENT", length = 4000)
    private String transitionComment;
    @Column(name = "TRANSITION_AT")
    private Timestamp transitionAt;
    @Column(name = "STATUS_CODE", length = 64, nullable = false)
    private String statusCode;
    @Column(name = "STATUS_NAME", length = 128, nullable = false)
    private String statusName;
    @Column(name = "TRANSITION_CODE", length = 64, nullable = false)
    private String transitionCode;
    @Column(name = "TRANSITION_NAME", length = 256, nullable = false)
    private String transitionName;
    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";
    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
