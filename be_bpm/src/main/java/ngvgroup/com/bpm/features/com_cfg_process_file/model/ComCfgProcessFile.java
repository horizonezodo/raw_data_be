package ngvgroup.com.bpm.features.com_cfg_process_file.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "COM_CFG_PROCESS_FILE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComCfgProcessFile extends BaseEntity {
    @Column(name = "PROCESS_FILE_CODE", nullable = false, length = 64)
    private String processFileCode;

    @Column(name = "PROCESS_FILE_NAME", nullable = false, length = 256)
    private String processFileName;

    @Column(name = "PROCESS_TYPE_CODE", nullable = false, length = 128)
    private String processTypeCode;

    @Column(name = "TAB_GROUP_CODE", length = 64)
    private String tabGroupCode;

    @Column(name = "TAB_GROUP_NAME", length = 128)
    private String tabGroupName;

    @Column(name = "FILE_TYPE_CODE", length = 64)
    private String fileTypeCode;

    @Column(name = "FILE_TYPE_NAME", length = 128)
    private String fileTypeName;

    @Column(name = "FILE_TYPE", length = 512)
    private String fileType;

    @Column(name = "FILE_SIZE")
    private Integer fileSize;

    @Column(name = "IS_PRINT")
    private Integer isPrint;

    @Column(name = "IS_UPLOAD")
    private Integer isUpload;

    @Column(name = "IS_AVATAR")
    private Integer isAvatar;

    @Column(name = "IS_SENT")
    private Integer isSent;

    @Column(name = "SORT_NUMBER")
    private Integer sortNumber;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

}