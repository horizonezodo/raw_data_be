package ngvgroup.com.bpm.core.base.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterpretiveStructureDto {
    private String contentCode;
    private Map<String, String> paramInterpretiveStructure;
}
