package ngvgroup.com.rpt.features.transactionreport.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import java.sql.Timestamp;

@Entity
@Table(name = "RPT_TXN_STAT_TEMPLATE_CHECK")
@Getter
@Setter
public class RptTxnStatTemplateCheck extends BaseEntity {
    @Column(name = "STAT_INSTANCE_CODE", nullable = false, length = 64)
    private String statInstanceCode;

    @Column(name = "RULE_CODE", nullable = false, length = 64)
    private String ruleCode;

    @Column(name = "RULE_NAME", nullable = false, length = 256)
    private String ruleName;

    @Column(name = "TEMPLATE_CODE", length = 128)
    private String templateCode;

    @Column(name = "KPI_CODE", length = 32)
    private String kpiCode;

    @Column(name = "RULE_MODE", nullable = false, length = 64)
    private String ruleMode;

    @Column(name = "RESPONSE_CODE", nullable = false, length = 64)
    private String responseCode;

    @Column(name = "ERROR_MSG", length = 4000)
    private String errorMsg;

    @Column(name = "DETECTED_AT")
    private Timestamp detectedAt;

    @Column(name = "DETECTED_BY", length = 64)
    private String detectedBy;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}

