package ngvgroup.com.fac.feature.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateDto {
    private String templateCode;
    private String fileName;
    private String description;
    private byte[] fileData;
    private String filePath;
    private BigDecimal fileSize;
    private String fileMappingPath;
}
