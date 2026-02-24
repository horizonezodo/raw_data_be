package ngvgroup.com.bpm.core.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrackingDto {
    private String taskName;
    private String status;
    private Date taskStartTime;
    private Date acceptedDate;
    private Date completeTime;
    private String executorCode;
    private String executorName;

    public TrackingDto(String taskName, String status, Date taskStartTime, Date acceptedDate, Date completeTime, String executorCode) {
        this.taskName = taskName;
        this.status = status;
        this.taskStartTime = taskStartTime;
        this.acceptedDate = acceptedDate;
        this.completeTime = completeTime;
        this.executorCode = executorCode;
    }
}
