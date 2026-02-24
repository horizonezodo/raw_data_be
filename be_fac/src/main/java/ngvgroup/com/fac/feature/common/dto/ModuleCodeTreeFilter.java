package ngvgroup.com.fac.feature.common.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ModuleCodeTreeFilter {
    private String moduleCode;
    private String moduleName;
    private List<ComCfgProcessTypeDTO> processTypes;
}
