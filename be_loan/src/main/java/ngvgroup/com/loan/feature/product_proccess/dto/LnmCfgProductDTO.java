package ngvgroup.com.loan.feature.product_proccess.dto;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class LnmCfgProductDTO {
    private Long id;
    private String orgCode;
    @ExcelColumn(value = LoanVariableConstants.PRODUCT_CODE)
    private String lnmProductCode;
    @ExcelColumn(value = LoanVariableConstants.PRODUCT_NAME)
    private String lnmProductName;
    @ExcelColumn(value = LoanVariableConstants.PRODUCT_TYPE_NAME)
    private String commonName;
    @ExcelColumn(value = LoanVariableConstants.INTEREST_RATE_NAME)
    private String interestRateName;
    @ExcelColumn(value = LoanVariableConstants.CURRENCY_TYPE)
    private String currencyCode;
    private String status;
}
