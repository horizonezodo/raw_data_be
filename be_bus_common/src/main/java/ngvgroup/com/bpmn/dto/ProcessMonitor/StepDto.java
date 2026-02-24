package ngvgroup.com.bpmn.dto.ProcessMonitor;

import java.util.Date;

public interface StepDto {

    String getProcInstId();
    String getActName();

    Date getStartTime();
    Date getEndTime();
    String getActId();
    String getState();
    String getCount();


}
