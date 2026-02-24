package ngvgroup.com.bpm.client.dto.request;

import lombok.Data;

@Data
public class SubmitTaskRequest<T> {
    private T businessData;
    private SubmitTaskBpmData bpmData;
}
