package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatekpi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgStatTemplateKpiDTO {
    private Long id;
    private String recordStatus;
    private String orgCode;
    private String templateCode;
    private String templateKpiCode;
    private String templateKpiName;
    private double sortExecute;
    private int sortNumber;
    private String dataKpiType;
    private double areaId;
    private double row;
    private double column;
    private String kpiCode;
    private String expressionSql;
    private double unitKpi;
    private double decimalScale;
    private double roundingDigits;
    private String kpiName;

}
