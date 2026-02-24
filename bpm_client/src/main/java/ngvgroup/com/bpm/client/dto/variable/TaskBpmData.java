package ngvgroup.com.bpm.client.dto.variable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ngvgroup.com.bpm.client.dto.shared.AuditDto;
import ngvgroup.com.bpm.client.dto.shared.ProcessFileDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskBpmData {
    private String currentUser;
    private String taskStatus;
    private String taskComment;
    private List<AuditDto> taskAudits;
    private List<ProcessFileDto> taskProcessFiles;
    private MailVariableDto mailVariable;
}
