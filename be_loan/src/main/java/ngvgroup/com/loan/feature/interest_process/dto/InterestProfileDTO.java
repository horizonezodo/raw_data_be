package ngvgroup.com.loan.feature.interest_process.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InterestProfileDTO {

    private Long id;

    @NotBlank
    private String orgCode;

    private String currencyCode;

    private String processInstanceCode;

    @NotBlank
    private String interestRateCode;

    @NotBlank
    private String interestRateName;

    @NotBlank
    private String interestRateType;

    @NotBlank
    private String applyType;

    private List<InterestHistoryDTO> history;
}
