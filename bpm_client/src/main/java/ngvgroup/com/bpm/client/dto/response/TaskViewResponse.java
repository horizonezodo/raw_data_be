package ngvgroup.com.bpm.client.dto.response;

import lombok.Data;

@Data
public class TaskViewResponse<T> {
    private T businessData;
    private TaskViewBpmData bpmData;
}
