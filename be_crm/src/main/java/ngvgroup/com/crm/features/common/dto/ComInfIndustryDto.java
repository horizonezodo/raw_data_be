package ngvgroup.com.crm.features.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComInfIndustryDto {
    private String industryCode;
    private String industryName;
}