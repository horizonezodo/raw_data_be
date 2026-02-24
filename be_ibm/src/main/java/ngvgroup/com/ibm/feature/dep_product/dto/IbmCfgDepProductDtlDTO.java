package ngvgroup.com.ibm.feature.dep_product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IbmCfgDepProductDtlDTO {
    private Long id;
    private String ibmDepProductCode;
    private String orgCode;
    private Date effectiveDate;
    private String interestCalcMethod;
    private String interestBaseCode;
    private String interestDateMethod;
    private Integer isPartialWithdraw;
    private Integer isEarlySettlement;
    private String interestStartMethod;
    private String maturityDateRule;

    private String interestCalcMethodName;
    private String interestDateMethodName;
    private String interestBaseCodeName;
}