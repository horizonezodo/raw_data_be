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
public class CompleteTaskRequestDto {
    
    private Map<String, CamundaVariableDto> variables;
}
