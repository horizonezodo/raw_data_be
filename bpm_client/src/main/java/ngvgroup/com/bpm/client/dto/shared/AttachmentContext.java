package ngvgroup.com.bpm.client.dto.shared;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachmentContext {
    private String processFileCode;
    private Map<String, Object> context;
}
