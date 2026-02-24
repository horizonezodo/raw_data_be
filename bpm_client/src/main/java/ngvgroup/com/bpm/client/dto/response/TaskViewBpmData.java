package ngvgroup.com.bpm.client.dto.response;

import lombok.Data;
import ngvgroup.com.bpm.client.dto.shared.AuditDto;
import ngvgroup.com.bpm.client.dto.shared.CommentDto;
import ngvgroup.com.bpm.client.dto.shared.ProcessFileDto;
import ngvgroup.com.bpm.client.dto.shared.TrackingDto;

import java.util.List;

@Data
public class TaskViewBpmData {
    private String processInstanceCode;
    private String businessStatus;
    private List<ProcessFileDto> taskProcessFiles;
    private List<CommentDto> taskComments;
    private List<TrackingDto> taskTrackings;
    private List<AuditDto> taskAudits;
}
