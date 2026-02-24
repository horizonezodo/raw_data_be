package ngvgroup.com.bpm.client.dto.variable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterpretiveStructureDto {
    private String contentCode;
    private Map<String, String> paramInterpretiveStructure;
}
