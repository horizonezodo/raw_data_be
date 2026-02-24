package ngvgroup.com.bpmn.dto.TxnTaskInbox;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComTxnTaskInboxResponse {
    private String taskId;
    private String procInstId_;
    private String processInstanceCode;
    private String taskDefineName;
    private String createdDate;
    private String taskUpdateTime;
    private String assignTo;
    private String businessStatus;
    private String ruleCode;
    private Boolean suspended;
}
