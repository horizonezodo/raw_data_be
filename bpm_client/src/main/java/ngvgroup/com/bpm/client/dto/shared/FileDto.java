package ngvgroup.com.bpm.client.dto.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {
    private String fileId;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String fileType;
}
