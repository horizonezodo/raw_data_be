package ngvgroup.com.bpmn.dto.TxnTaskInbox;

import java.sql.Timestamp;

public interface ActHiTaskIntResponse {
    String getTaskId();
    String getProcInstId_();
    String getProcessInstanceCode();
    String getTaskDefineName();
    Timestamp getCreatedDate();
    Timestamp getTaskUpdateTime();
    String getAssignTo();
    String getBusinessStatus();
    String getRuleCode();
    Boolean getIsSuspended();
}
