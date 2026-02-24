package ngvgroup.com.bpm.features.sla.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ComCfgSlaTaskDto {

    private String priorityLevel;


    private String orgCode;
    private String taskDefineCode;
    private String processDefineCode;
    private String unit;


}
