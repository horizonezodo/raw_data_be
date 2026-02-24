package ngvgroup.com.loan.feature.interest_process.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InterestHistoryDetailDTO {

    private Long id;

    private BigDecimal amtFrom;

    private BigDecimal amtTo;

    private BigDecimal interestRate;

    private BigDecimal interestRateMin;

    private BigDecimal interestRateMax;

    private BigDecimal floorInterestRate;

    private BigDecimal capInterestRate;

    private String resetFreq;

    private String baseRateCode;

    private BigDecimal spread;
}
