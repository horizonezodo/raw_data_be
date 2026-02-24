package ngvgroup.com.bpmn.dto.ProcessMonitor;

import java.util.Date;

public interface ProcessMonitorDto {
    String getName();
    String getProcInstId();
    String getBusinessKey();
    Date getStartTime();
    Date getEndTime();
    String getState();


    //
    String getSuperExec();
    String getResourceName();
    String getDeploymentId();
}
