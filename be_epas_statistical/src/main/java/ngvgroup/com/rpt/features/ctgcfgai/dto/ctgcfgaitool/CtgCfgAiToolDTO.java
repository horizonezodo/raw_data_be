package ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgAiToolDTO {
    private Integer active;
    private String toolAiCode;
    private String toolAiName;
    private String toolAiTypeCode;
    private String toolAiTypeName;
    private String inputParameter;
    private String templateConfig;
    private String outputFormat;
    private double timeoutSecond;
    private int retryCount;
    private boolean requiresAuth;
    private String allowedRole;
    private String description;
}
