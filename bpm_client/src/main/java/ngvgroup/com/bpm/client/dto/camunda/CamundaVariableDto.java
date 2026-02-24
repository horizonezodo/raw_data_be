package ngvgroup.com.bpm.client.dto.camunda;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CamundaVariableDto {
  private Object value;
  private String type;
  private Map<String, Object> valueInfo; // optional
}
