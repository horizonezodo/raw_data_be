package ngvgroup.com.crm.features.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComCfgCommonDto {
    private String commonCode; // Giá trị lưu (Value)
    private String commonName; // Giá trị hiển thị (Label)
}