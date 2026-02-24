package ngvgroup.com.bpm.features.com_txn_task_inbox.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignedTaskDto {
    private String processInstanceCode;
    private String taskDefineName;
    private String acceptedBy;
}
