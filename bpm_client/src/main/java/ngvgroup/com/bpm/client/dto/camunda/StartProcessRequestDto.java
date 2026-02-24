package ngvgroup.com.bpm.client.dto.camunda;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StartProcessRequestDto {
    private String businessKey;
    private String userId;
    private Map<String, Object> variables;
}
