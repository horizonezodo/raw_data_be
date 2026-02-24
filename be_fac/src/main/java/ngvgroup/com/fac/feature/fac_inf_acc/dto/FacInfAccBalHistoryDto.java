package ngvgroup.com.fac.feature.fac_inf_acc.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FacInfAccBalHistoryDto {
    private Integer orderNo;
    private LocalDate dataDate;
    private BigDecimal openingBal;
    private BigDecimal totalDrAmt;
    private BigDecimal totalCrAmt;
    private BigDecimal closingBal;
}
