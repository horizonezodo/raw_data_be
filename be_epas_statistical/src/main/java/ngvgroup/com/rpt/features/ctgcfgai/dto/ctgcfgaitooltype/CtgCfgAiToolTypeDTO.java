package ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitooltype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgAiToolTypeDTO
{
    private String toolAiTypeCode;
    private String toolAiTypeName;
    private String description;
    private Integer active;

    public CtgCfgAiToolTypeDTO(String toolAiTypeCode, String toolAiTypeName, String description) {
        this.toolAiTypeCode = toolAiTypeCode;
        this.toolAiTypeName = toolAiTypeName;
        this.description = description;
    }
}
