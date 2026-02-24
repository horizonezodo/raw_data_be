package ngvgroup.com.bpmn.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "CTG_CFG_REPORT_PARAM_COND")
public class CtgCfgReportParamCond extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "RECORD_STATUS", length = 64, nullable = false)
    private String recordStatus;

    @Column(name = "IS_ACTIVE", nullable = false)
    private int isActive;

    @Column(name = "REPORT_CODE", nullable = false, length = 128)
    private String reportCode;

    @Column(name = "SOURCE_PARAM_CODE", nullable = false, length = 128)
    private String sourceParamCode;

    @Column(name = "SOURCE_PARAM_VALUE", nullable = false, length = 128)
    private String sourceParamValue;

    @Column(name = "TARGET_PARAM_CODE", nullable = false, length = 128)
    private String targetParamCode;

    @Column(name = "CONDITION_TYPE", nullable = false, length = 128)
    private String conditionType;

    @Column(name = "EXPRESSION", length = 4000)
    private String expression;

    @Column(name = "SORT_NUMBER")
    private Long sortNumber;

    @Column(name = "DESCRIPTION", length = 512)
    private String description;

}
