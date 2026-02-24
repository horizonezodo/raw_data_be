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
@Table(name = "CTG_CFG_STAT_TEMPLATE_KPI")
public class CtgCfgStatTemplateKpi extends BaseEntity {
    @Column(name = "ORG_CODE",length = 64, nullable = false)
    private String orgCode;

    @Column(name = "TEMPLATE_CODE",length = 128, nullable = false)
    private String templateCode;

    @Column(name = "TEMPLATE_KPI_CODE",length = 64, nullable = false)
    private String templateKpiCode;

    @Column(name = "TEMPLATE_KPI_NAME",length = 128, nullable = false)
    private String templateKpiName;

    @Column(name = "SORT_EXECUTE",length = 5)
    private double sortExecute;

    @Column(name = "SORT_NUMBER",length = 22)
    private int sortNumber;

    @Column(name = "DATA_KPI_TYPE",length = 64)
    private String dataKpiType;

    @Column(name = "AREA_ID",length = 2)
    private double areaId;

    @Column(name = "ROW_INDEX",length = 5)
    private double row;

    @Column(name = "COLUMN_INDEX",length = 5)
    private double column;

    @Column(name = "KPI_CODE",length = 32)
    private String kpiCode;

    @Column(name = "EXPRESSION_SQL",length = 4000)
    private String expressionSql;

    @Column(name = "UNIT_KPI",length = 5)
    private double unitKpi;

    @Column(name = "DECIMAL_SCALE",length = 1)
    private double decimalScale;

    @Column(name = "ROUNDING_DIGITS",length = 3)
    private double roundingDigits;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
