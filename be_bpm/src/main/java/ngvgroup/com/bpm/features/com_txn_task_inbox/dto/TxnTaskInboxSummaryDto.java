package ngvgroup.com.bpm.features.com_txn_task_inbox.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TxnTaskInboxSummaryDto {
    private String assignee;
    private String assigneeUsername;
    private String assigneeRuleCode;
    private int notStarted;
    private int inProgress;
    private int completed;
    private int total;
    private Boolean isAssigned;
}
