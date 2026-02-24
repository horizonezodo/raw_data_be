package ngvgroup.com.bpmn.dto.TxnTaskInbox;

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
