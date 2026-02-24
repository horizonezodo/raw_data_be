package ngvgroup.com.bpm.core.base.dto;

import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * DTO matching Camunda engine-rest variable format
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CamundaVariableDto {
    private Object value;
    private String type;
    private Map<String, Object> valueInfo;
}
