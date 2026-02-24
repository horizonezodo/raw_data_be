package ngvgroup.com.rpt.features.transactionreport.dto.sub;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReportResultDto {
    //OPN_DR_BAL, OPN_CR_BAL, INCR_DR_BAL, INCR_CR_BAL, CLO_DR_BAL, CLO_CR_BAL
    private BigDecimal opnDrBal;
    private BigDecimal opnCrBal;
    private BigDecimal incrDrBal;
    private BigDecimal incrCrBal;
    private BigDecimal cloDrBal;
    private BigDecimal cloCrBal;
}
