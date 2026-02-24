package ngvgroup.com.bpm.features.com_txn_task_inbox.dto;

import java.sql.Timestamp;

public interface ActHiTaskIntResponse {
    String getTaskId();
    String getProcInstId();
    String getProcessInstanceCode();
    String getTaskDefineName();
    Timestamp getCreatedDate();
    Timestamp getTaskUpdateTime();
    String getAssignTo();
    String getBusinessStatus();
    String getRuleCode();
    Boolean getIsSuspended();
}
