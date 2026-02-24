package ngvgroup.com.bpm.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartResponse {
    private String processInstanceCode;
    private String processInstanceId;
}
