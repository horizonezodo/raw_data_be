package ngvgroup.com.rpt.features.ctgcfgstattemplate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CTG_CFG_STAT_TYPE")
public class CtgCfgStatType extends BaseEntity {
    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;

    @Column(name = "STAT_TYPE_CODE", length = 64, nullable = false)
    private String statTypeCode;

    @Column(name = "STAT_TYPE_NAME", length = 256, nullable = false)
    private String statTypeName;

    @Column(name = "REPORT_MODULE_CODE", length = 64)
    private String reportModuleCode;

    @Column(name = "EXPRESSION_SQL", length = 4000)
    private String expressionSql;

    @Column(name = "SORT_NUMBER", length = 22)
    private int sortNumber;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
