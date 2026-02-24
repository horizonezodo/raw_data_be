package ngvgroup.com.bpm.client.dto.request;

import lombok.Data;

@Data
public class DraftTaskBpmRequest <T> {
    private T businessData;
    private DraftTaskBpmData bpmData;
}
