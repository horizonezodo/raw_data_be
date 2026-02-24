package ngvgroup.com.bpmn.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "CTG_CFG_REPORT_PARAM_BASE")
public class CtgCfgReportParamBase extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "RECORD_STATUS", length = 64, nullable = false)
    private String recordStatus;

    @Column(name = "IS_ACTIVE", nullable = false)
    private int isActive;

    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "PARAM_BASE_CODE", nullable = false, length = 128)
    private String paramBaseCode;

    @Column(name = "PARAM_BASE_NAME", nullable = false, length = 256)
    private String paramBaseName;

    @Column(name = "PARAM_BASE_TYPE", nullable = false, length = 16)
    private String paramBaseType;
}
