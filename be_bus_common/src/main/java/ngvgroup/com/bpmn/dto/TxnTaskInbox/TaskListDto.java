package ngvgroup.com.bpmn.dto.TxnTaskInbox;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskListDto {
    private String processInstanceCode; // Mã công việc
    private String processTypeName; // Loại giao dịch
    private String taskDefineName; // Tên công việc
    private Integer slaMaxDuration; // Thời gian cam kết nghiệp vụ
    private String acceptedDate; // Thời gian tiếp nhận
    private String slaTaskDeadline; // Thời hạn
    private String assignee; // Người thực hiện
    private String businessStatus; // Trạng thái
}
