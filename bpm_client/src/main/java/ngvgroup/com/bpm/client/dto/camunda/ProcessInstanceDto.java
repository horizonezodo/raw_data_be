package ngvgroup.com.bpm.client.dto.camunda;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessInstanceDto {
   private String id;
   @JsonProperty("definitionId")
   private String processDefinitionId;
   private String businessKey;
   private boolean ended;
   private boolean suspended;
}
