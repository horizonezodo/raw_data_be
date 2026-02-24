package ngvgroup.com.rpt.features.ctgcfgstatruledefine.model;

import jakarta.persistence.*;
import lombok.*;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import java.util.Date;


@Entity
@Table(name = "CTG_CFG_STAT_RULE_DEFINE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatRuleDefine extends BaseEntity {
    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;

    @Column(name = "RULE_CODE", length = 64, nullable = false)
    private String ruleCode;

    @Column(name = "RULE_NAME", length = 256, nullable = false)
    private String ruleName;

    @Column(name = "RULE_TYPE_CODE", length = 64, nullable = false)
    private String ruleTypeCode;

    @Column(name = "EXPRESSION_SQL", length = 4000)
    private String expressionSql;

    @Column(name = "RESPONSE_CODE", length = 64, nullable = false)
    private String responseCode;

    @Column(name = "RULE_MODE", length = 64, nullable = false)
    private String ruleMode;

    @Temporal(TemporalType.DATE)
    @Column(name = "EFFECTIVE_DATE", nullable = false)
    private Date effectiveDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "EXPIRY_DATE")
    private Date expiryDate;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
