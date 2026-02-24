package ngvgroup.com.bpmn.dto.TxnTaskInbox;

import java.sql.Timestamp;

public interface InboundTransactionsDto {
    String getCustomerCode();
    String getCustomerName();
    String getProcessInstanceCode();
    String getOrgName();
    String getTaskName();
    Timestamp getTaskStartTime();
    String getUserBefore();
    // taskId
    String getTaskId();
    String getProcInstId();
}
