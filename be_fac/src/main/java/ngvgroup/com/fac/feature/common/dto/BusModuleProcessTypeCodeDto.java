package ngvgroup.com.fac.feature.common.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BusModuleProcessTypeCodeDto {
    private String moduleCode;
    private String moduleName;
    private String processTypeCode;
    private String processTypeName;
}
