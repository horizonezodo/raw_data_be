package ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IbmCfgDepIntRateDtlDtTO {
    private Long id;

    @NotNull(message = "Ngày hiệu lực không được để trống")
    private Date effectiveDate;

    private BigDecimal interestRate;

    private BigDecimal interestRateMin;

    private BigDecimal interestRateMax;
}
