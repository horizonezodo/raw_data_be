package ngvgroup.com.bpm.core.base.dto;

import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StartProcessRequestDto {
    private Map<String, Object> variables;
    private String businessKey;
    private String userId;
}
