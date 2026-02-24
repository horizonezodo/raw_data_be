package ngvgroup.com.crm.features.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComInfAreaDto {
    private String areaCode;
    private String areaName;
    private String wardCode;

    ComInfAreaDto(String areaCode, String areaName) {
        this.areaCode = areaCode;
        this.areaName = areaName;
    }
}