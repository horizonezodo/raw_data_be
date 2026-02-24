package ngvgroup.com.crm.features.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComInfProvinceDto {
    private String provinceCode;
    private String provinceName;
}