package ngvgroup.com.bpm.client.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ngvgroup.com.bpm.client.dto.shared.AuditDto;
import ngvgroup.com.bpm.client.dto.shared.ProcessFileDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DraftTaskBpmData {
    private String taskId;
    private String processInstanceCode;
    private List<ProcessFileDto> existingProcessFiles;
    private List<AuditDto> taskAudits;
}
