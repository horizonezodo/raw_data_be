package ngvgroup.com.bpmn.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "CTG_CFG_REPORT_PARAM")
public class CtgCfgReportParam extends BaseEntity {

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

    @Column(name = "PARAMETER_CODE", length = 128)
    private String parameterCode;

    @Column(name = "PARAMETER_NAME", length = 256)
    private String parameterName;

    @Column(name = "PARAMETER_NAME_EN", length = 256)
    private String parameterNameEn;

    @Column(name = "PARAMETER_TYPE", length = 16)
    private String parameterType;

    @Column(name = "CONTROL_TYPE", length = 64)
    private String controlType;

    @Column(name = "RESOURCE_SQL", length = 4000)
    private String resourceSql;

    @Column(name = "IS_DISPLAY")
    private Integer isDisplay;

    @Column(name = "SORT_NUMBER")
    private Long sortNumber;

    @Column(name = "DISPLAY_FRAME", length = 16)
    private String displayFrame;

}
