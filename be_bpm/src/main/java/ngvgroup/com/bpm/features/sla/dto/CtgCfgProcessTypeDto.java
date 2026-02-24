package ngvgroup.com.bpm.features.sla.dto;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgProcessTypeDto {
    private String processTypeCode;
    private String processTypeName;
    private Integer isAccounting;

    public CtgCfgProcessTypeDto(String processTypeCode, String processTypeName) {
        this.processTypeCode = processTypeCode;
        this.processTypeName = processTypeName;
    }
}
