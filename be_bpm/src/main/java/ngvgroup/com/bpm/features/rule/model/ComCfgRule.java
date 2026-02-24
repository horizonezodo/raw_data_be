package ngvgroup.com.bpm.features.rule.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COM_CFG_RULE")
public class ComCfgRule extends BaseEntity {
    @Column(name = "RULE_CODE", nullable = false, length = 64)
    private String ruleCode;
    @Column(name = "RULE_NAME", nullable = false, length = 256)
    private String ruleName;
    @Column(name = "PARENT_CODE", length = 128)
    private String parentCode;
    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;
    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus;
    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private Integer isActive;


    public ComCfgRule(String ruleCode, String ruleName, String parentCode, String orgCode) {
        this.ruleCode = ruleCode;
        this.ruleName = ruleName;
        this.parentCode = parentCode;
        this.orgCode = orgCode;
    }
}
