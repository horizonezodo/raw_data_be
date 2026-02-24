package ngvgroup.com.bpm.features.com_txn_task_inbox.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
public class InOutTransDto {
    private String processTypeCode;
    private String processInstanceCode;
    private String customerCode;
    private String orgCode;
    private String fromDate;
    private String toDate;
}
