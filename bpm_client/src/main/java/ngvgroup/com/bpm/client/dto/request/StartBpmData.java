package ngvgroup.com.bpm.client.dto.request;

import lombok.Data;
import ngvgroup.com.bpm.client.dto.shared.AuditDto;
import ngvgroup.com.bpm.client.dto.shared.ProcessFileDto;

import java.util.List;

@Data
public class StartBpmData {
    private String processDefinitionKey;
    private String taskComment;
    private List<AuditDto> taskAudits;
    private List<ProcessFileDto> existingProcessFiles;
}
