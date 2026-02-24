package ngvgroup.com.bpm.features.com_txn_task_inbox.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ngvgroup.com.bpm.core.utils.LowerCaseDeserializer;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComTxnTaskInboxSearch {
    @JsonDeserialize(using = LowerCaseDeserializer.class)
    String taskId;
    @JsonDeserialize(using = LowerCaseDeserializer.class)
    String procInstId;
    @JsonDeserialize(using = LowerCaseDeserializer.class)
    String processInstanceCode;
    String createdDateStart;
    String createdDateEnd;
    String taskUpdateTimeStart;
    String taskUpdateTimeEnd;
}
