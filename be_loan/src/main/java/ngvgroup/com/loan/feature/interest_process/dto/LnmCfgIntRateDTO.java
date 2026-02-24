package ngvgroup.com.loan.feature.interest_process.dto;


import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;

@Getter
@Setter
@AllArgsConstructor
public class LnmCfgIntRateDTO {

    private Long id;

    private String orgCode;

    @ExcelColumn(value = LoanVariableConstants.INTEREST_RATE_CODE)
    private String interestRateCode;

    @ExcelColumn(value = LoanVariableConstants.INTEREST_RATE_NAME)
    private String interestRateName;

    @ExcelColumn(value = LoanVariableConstants.INTEREST_RATE_TYPE_NAME)
    private String commonName;

    @ExcelColumn(value = LoanVariableConstants.CURRENCY_TYPE)
    private String currencyCode;

    // BE trả text luôn
    private String statusName;

}
