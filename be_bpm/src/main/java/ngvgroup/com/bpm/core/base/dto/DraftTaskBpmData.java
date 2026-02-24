package ngvgroup.com.bpm.core.base.dto;

import java.util.List;

import lombok.Data;

@Data
public class DraftTaskBpmData {
    private String taskId;
    private List<ProcessFileDto> taskProcessFiles;
    private List<AuditDto> taskAudits;
}
