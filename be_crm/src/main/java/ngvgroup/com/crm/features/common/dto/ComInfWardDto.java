package ngvgroup.com.crm.features.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComInfWardDto {
    private String wardCode;
    private String wardName;
    // Có thể cần thêm provinceCode nếu FE muốn map lại client-side, 
    // nhưng nếu chỉ để hiển thị list sau khi đã lọc thì không cần.
}