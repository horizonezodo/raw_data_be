package ngvgroup.com.fac.feature.sheet_import_export_process.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDto {
    private String fileCode;

    private String fileName;

    private Integer sortNumber;

    private String fileContentBase64;

    private String fileExtension;

    private Integer isUpload;
}
