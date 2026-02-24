package ngvgroup.com.bpmn.model;

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
@Table(name = "COM_CFG_RULE_USER")
public class ComCfgRuleUser extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rule_seq")
    @SequenceGenerator(name = "rule_seq", sequenceName = "rule_seq", allocationSize = 1)
    private Long id;
    @Column(name = "RULE_CODE", nullable = false, length = 64)
    private String ruleCode;
    @Column(name = "USER_ID", nullable = false, length = 64)
    private String userId;
    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;
    @Column(name = "RECORD_STATUS",nullable = false,length = 64)
    private String recordStatus;
    @Column(name = "IS_ACTIVE",nullable = false)
    private int isActive;

    public ComCfgRuleUser(String ruleCode, String userId, String orgCode){
        this.ruleCode = ruleCode;
        this.userId = userId;
        this.orgCode = orgCode;
    }
}
