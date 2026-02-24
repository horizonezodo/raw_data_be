package ngvgroup.com.bpmn.dto.TxnTaskInbox;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ngvgroup.com.bpmn.utils.LowerCaseDeserializer;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComTxnTaskInboxSearch {
    @JsonDeserialize(using = LowerCaseDeserializer.class)
    String taskId;
    @JsonDeserialize(using = LowerCaseDeserializer.class)
    String procInstId_;
    @JsonDeserialize(using = LowerCaseDeserializer.class)
    String processInstanceCode;
    String createdDateStart;
    String createdDateEnd;
    String taskUpdateTimeStart;
    String taskUpdateTimeEnd;
}
