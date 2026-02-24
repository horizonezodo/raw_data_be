package ngvgroup.com.bpm.core.base.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BPM_TXN_DOC_FILE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BpmTxnDocFile extends BaseEntity {

    @Column(name = "TXN_DATE", nullable = false)
    private java.util.Date txnDate;

    @Column(name = "PROCESS_TYPE_CODE", nullable = false, length = 128)
    private String processTypeCode;

    @Column(name = "PROCESS_INSTANCE_CODE", nullable = false, length = 128)
    private String processInstanceCode;

    @Column(name = "TASK_ID", nullable = false, length = 128)
    private String taskId;

    @Column(name = "REFERENCE_CODE", length = 128)
    private String referenceCode;

    @Column(name = "CUSTOMER_CODE", length = 128)
    private String customerCode;

    @Column(name = "PROCESS_FILE_CODE", length = 64)
    private String processFileCode;

    @Column(name = "FILE_NAME", length = 256)
    private String fileName;

    @Column(name = "FILE_SIZE")
    private Long fileSize;

    @Column(name = "FILE_ID", length = 256)
    private String fileId;

    @Column(name = "FILE_PATH", length = 256)
    private String filePath;

    @Column(name = "IS_AVATAR", nullable = false)
    private Integer isAvatar = 0;

    @Column(name = "IS_SENT", nullable = false)
    private Integer isSent = 0;

    @Column(name = "IS_PRINT", nullable = false)
    private Integer isPrint = 0;

    @Column(name = "BUSINESS_STATUS", nullable = false, length = 32)
    private String businessStatus;
}
