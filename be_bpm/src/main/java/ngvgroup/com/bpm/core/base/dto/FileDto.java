package ngvgroup.com.bpm.core.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDto {
    private String fileId;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String fileType;

    public FileDto(String fileId, String fileName, String filePath, Long fileSize) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

}