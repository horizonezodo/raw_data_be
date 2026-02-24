package ngvgroup.com.bpm.client.dto.request;

import lombok.Data;
import ngvgroup.com.bpm.client.dto.shared.AuditDto;
import ngvgroup.com.bpm.client.dto.shared.ProcessFileDto;

import java.util.List;

@Data
public class SubmitTaskBpmData {
    private String taskId;
    private String taskStatus;
    private String taskComment;
    private String processInstanceCode;
    private List<AuditDto> taskAudits;
    private List<ProcessFileDto> existingProcessFiles;
}
