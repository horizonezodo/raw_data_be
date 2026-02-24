package ngvgroup.com.bpm.core.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComCfgParameterDto {
    private String paramCode;
    private String paramName;
    private String paramValue;
    private String valueDescription;
    private String orgCode;
    private String paramType;
    private String moduleCode;
    private String moduleName;
    private String paramDefaultValue;
}
