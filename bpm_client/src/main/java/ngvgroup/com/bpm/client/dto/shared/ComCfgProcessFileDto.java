package ngvgroup.com.bpm.client.dto.shared;

import lombok.Data;

@Data
public class ComCfgProcessFileDto {
    private String processFileCode;
    private String processFileName;
    private Integer fileSize;
    private Integer isAvatar;
    private Integer isSent;
    private Integer isPrint;
    private Integer isUpload;
    private String fileType;
    private String fileTypeCode;
}
