package ngvgroup.com.bpm.core.base.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskBpmData {
    private String currentUser;
    private String taskStatus;
    private String taskComment;
    private List<AuditDto> taskAudits;
    private List<ProcessFileDto> taskProcessFiles;
    private MailVariableDto mailVariable;
}
