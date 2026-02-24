package ngvgroup.com.bpm.core.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrmInfoDto {
    private String username;
    private String employeeName;
    private String positionName;
}
