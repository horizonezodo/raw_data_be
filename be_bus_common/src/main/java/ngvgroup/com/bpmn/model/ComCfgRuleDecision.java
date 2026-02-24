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
@Table(name = "COM_CFG_RULE_DECISION")
public class ComCfgRuleDecision extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rule_seq")
    @SequenceGenerator(name = "rule_seq", sequenceName = "rule_seq", allocationSize = 1)
    private Long id;
    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;
    @Column(name = "PROCESS_TYPE_CODE", length = 128)
    private String processTypeCode;
    @Column(name = "PROCESS_TYPE_NAME",  length = 256)
    private String processTypeName;
    @Column(name = "TASK_DEFINE_CODE",  length = 128)
    private String taskDefineCode;
    @Column(name = "TASK_DEFINE_NAME",  length = 256)
    private String taskDefineName;
    @Column(name = "RULE_CODE",  length = 64)
    private String ruleCode;
    @Column(name = "RULE_NAME", length = 256)
    private String ruleName;
    @Column(name = "PARA_01", length = 256)
    private String para1;
    @Column(name = "PARA_02",  length = 256)
    private String para2;
    @Column(name = "PARA_03", length = 256)
    private String para3;
    @Column(name = "PARA_04", length = 256)
    private String para4;
    @Column(name = "PARA_05",  length = 256)
    private String para5;
    @Column(name = "PARA_06", length = 256)
    private String para6;
    @Column(name = "PARA_07", length = 256)
    private String para7;
    @Column(name = "PARA_08", length = 256)
    private String para8;
    @Column(name = "PARA_09",  length = 256)
    private String para9;
    @Column(name = "PARA_10", length = 256)
    private String para10;
    @Column(name = "RECORD_STATUS",nullable = false,length = 64)
    private String recordStatus;
    @Column(name = "IS_ACTIVE",nullable = false)
    private int isActive;

}
