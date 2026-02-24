package ngvgroup.com.bpmn.dto.TxnTaskInbox;

import java.sql.Timestamp;

public interface OutboundTransactionsDto {
    String getCustomerCode();
    String getCustomerName();
    String getProcessInstanceCode();
    String getOrgName();
    String getTaskName();
    Timestamp getTaskStartTime();
    Timestamp getEndTime();
    String getUserBefore();
    // taskId
    String getTaskId();
    String procInstId();
}
