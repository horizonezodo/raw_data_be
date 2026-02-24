package ngvgroup.com.loan.feature.product_proccess.dto;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor @AllArgsConstructor
public class LnmTxnProductDtlDTO {

    private Long id;

    @NotBlank
    @ExcelColumn(value = LoanVariableConstants.EFFECTIVE_DATE)
    private Date effectiveDate;

    private Integer principalGraceDay;
    private Integer interestGraceDay;

    @NotBlank
    private String maturityDateRule;

    @NotBlank
    private String interestPaymentMethod;

    private Integer isAccruedCalc;
    @ExcelColumn(value = LoanVariableConstants.INTEREST_CALC_METHOD)
    private String interestCalcMethod;
    @ExcelColumn(value = LoanVariableConstants.INTEREST_BASE_CODE)
    private String interestBaseCode;
    private Integer isInterestDiffMgmt;
    @ExcelColumn(value = LoanVariableConstants.INTEREST_DATE_METHOD)
    private String interestDateMethod;
    private String interestStartMethod;
    private String overdueCheckCond;
    private String overdueInterestType;
    private BigDecimal overdueInterestValue;
    private String penaltyInterestType;
    private BigDecimal penaltyInterestValue;
}
