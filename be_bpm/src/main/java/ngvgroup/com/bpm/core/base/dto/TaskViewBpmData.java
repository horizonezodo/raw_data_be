package ngvgroup.com.bpm.core.base.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class TaskViewBpmData {
    private String processInstanceCode;
    private String businessStatus;
    private List<ProcessFileDto> taskProcessFiles;
    private List<CommentDto> taskComments;
    private List<TrackingDto> taskTrackings;
    private List<AuditDto> taskAudits;
}
