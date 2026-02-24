package ngvgroup.com.bpm.features.com_txn_task_inbox.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComTxnTaskInboxResponse {
    private String taskId;
    private String procInstId;
    private String processInstanceCode;
    private String taskDefineName;
    private String createdDate;
    private String taskUpdateTime;
    private String assignTo;
    private String businessStatus;
    private String ruleCode;
    private Boolean suspended;
}
