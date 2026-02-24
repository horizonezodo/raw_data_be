package ngvgroup.com.bpmn.dto.ProcessMonitor;

import java.util.Date;

public interface StepInfoDto {
    String getActName();
    Date getStartTime();
    Date getEndTime();
    Integer getActCount();

}
