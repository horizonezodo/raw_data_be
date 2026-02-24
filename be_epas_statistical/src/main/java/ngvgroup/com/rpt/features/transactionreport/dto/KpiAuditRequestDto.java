package ngvgroup.com.rpt.features.transactionreport.dto;

import lombok.Data;

@Data
public class KpiAuditRequestDto {
    private String statInstanceCode;
    private String kpiCode;
    private String kpiName;
    private String kpiOldValue;
    private String kpiNewValue;
    private String editReason;
    private String templateCode;
    private String templateKpiCode;
}
