package ngvgroup.com.rpt.features.ctgcfgstatus.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgStatusSlaDto {
    private String statusCode; // CTG_CFG_STATUS.STATUS_CODE
    private Integer durationTime; // Number(4)
    private Integer graceTime; // Number(4)
    private Integer useBusinessTime; // 1: business hours, 0: 24/7
    private Integer warningBeforeTime;
    private Integer escalateAfterTime;
    private String warningSql;
    private String escalateSql;
    private Integer isEnable; // 1/0
    private String timeUnit; // ví dụ: MINUTE/HOUR/DAY
    private Date effectiveDate;
    private Date expiryDate;
}
