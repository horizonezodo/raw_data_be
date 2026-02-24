package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatedeadline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgStatTemplateDeadLineDTO {
    private Long id;
    private String templateCode;
    private String frequency;
    private String deadlineType;
    private String deadlineValue;
    private String deadlineDayTime;

    public CtgCfgStatTemplateDeadLineDTO(String frequency, String deadlineType, String deadlineValue, String deadlineDayTime) {
        this.frequency = frequency;
        this.deadlineType = deadlineType;
        this.deadlineValue = deadlineValue;
        this.deadlineDayTime = deadlineDayTime;
    }
}
