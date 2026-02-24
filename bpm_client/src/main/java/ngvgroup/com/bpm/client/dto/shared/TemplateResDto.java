package ngvgroup.com.bpm.client.dto.shared;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TemplateResDto {
    private String templateCode;
    private String fileName;
    private String description;
    private byte[] fileData;
    private String filePath;
    private BigDecimal fileSize;
    private String fileMappingPath;
}
