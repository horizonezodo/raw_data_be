package ngvgroup.com.fac.feature.ctg_cfg_acc_class.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FacCfgAccClassCoaMapDto {
    private Long id;
    private String orgCode;
    private Date effectiveDate;
    private String coaVersionCode;
    private String accClassCode;
    private String accCoaCode;
    private String debtGroupCode;
    private String currencyCode;
    private String channelCode;
    private String accCoaName;
}
