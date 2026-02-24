package ngvgroup.com.fac.feature.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TemplateResDto {
    private String templateCode;

    private String fileName;

    private String description;

    private byte[] fileData;

    private String filePath;

    private BigDecimal fileSize;

    private String mappingFilePath;

    public TemplateResDto(String templateCode, String fileName, String description, String filePath, BigDecimal fileSize, String mappingFilePath) {
        this.templateCode = templateCode;
        this.fileName = fileName;
        this.description = description;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mappingFilePath = mappingFilePath;
    }
}
