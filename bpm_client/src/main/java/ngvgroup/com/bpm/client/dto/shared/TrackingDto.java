package ngvgroup.com.bpm.client.dto.shared;

import lombok.Data;
import java.util.Date;

@Data
public class TrackingDto {
    private String taskName;
    private String status;
    private Date taskStartTime;
    private Date acceptedDate;
    private Date completeTime;
    private String executorCode;
    private String executorName;
}
