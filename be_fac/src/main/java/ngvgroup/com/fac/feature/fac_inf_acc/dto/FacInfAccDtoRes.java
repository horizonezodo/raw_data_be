package ngvgroup.com.fac.feature.fac_inf_acc.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FacInfAccDtoRes {
    private Long id;

    private String accNo;

    private String accName;

    private BigDecimal bal;

    private String accNature;

    private String accStatus;

    private BigDecimal balAvailable;

    private BigDecimal balActual;

    private String currencyCode;

    private String accClassCode;
}
