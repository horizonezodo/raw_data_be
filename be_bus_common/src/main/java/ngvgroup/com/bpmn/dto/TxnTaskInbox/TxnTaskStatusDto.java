package ngvgroup.com.bpmn.dto.TxnTaskInbox;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TxnTaskStatusDto {
    private int notStarted;
    private int inProgress;
    private int completed;
    private int overdue;

    public TxnTaskStatusDto() {
        this.notStarted = 0;
        this.inProgress = 0;
        this.completed = 0;
        this.overdue = 0;
    }
}
