package ngvgroup.com.bpm.core.base.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
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