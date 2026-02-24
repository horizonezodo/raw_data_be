package ngvgroup.com.bpm.client.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartRequest<T> {
    private T businessData;
    private StartBpmData bpmData;
}
