package ngvgroup.com.bpmn.dto.ProcessMonitor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.bpmn.dto.PageDTO.PageableDTO;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchProcessMonitorDto {
    private String procDefId;
    private String procInstId;
    private String businessKey;
    private String fromStartTime;
    private String toStartTime;
    private String fromEndTime;
    private String toEndTime;
    private String state;
    private PageableDTO pageable;
}
