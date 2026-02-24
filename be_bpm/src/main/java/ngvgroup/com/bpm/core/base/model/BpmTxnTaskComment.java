package ngvgroup.com.bpm.core.base.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@Entity
@Table(name = "BPM_TXN_TASK_COMMENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BpmTxnTaskComment extends BaseEntity {
    @Column(name = "TXN_DATE", nullable = false)
    private Date txnDate;

    @Column(name = "PROCESS_INSTANCE_CODE", nullable = false, length = 128)
    private String processInstanceCode;

    @Column(name = "PROCESS_TYPE_CODE", nullable = false, length = 128)
    private String processTypeCode;

    @Column(name = "TASK_ID", nullable = false, length = 128)
    private String taskId;

    @Column(name = "TASK_STATUS", length = 64)
    private String taskStatus;

    @Column(name = "TASK_COMMENTS", length = 512)
    private String taskComments;

    @Column(name = "BUSINESS_STATUS", length = 32, nullable = false)
    private String businessStatus;

}
