package ngvgroup.com.bpmn.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.bpmn.constant.EntityStatus;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CTG_CFG_REPORT_GROUP")
public class CtgCfgReportGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "RECORD_STATUS", length = 64, nullable = false)
    private String recordStatus;

    @Column(name = "IS_ACTIVE", nullable = false)
    private int isActive;

    @Column(name = "REPORT_GROUP_CODE", nullable = false, length = 128)
    private String reportGroupCode;

    @Column(name = "REPORT_GROUP_NAME", nullable = false, length = 256)
    private String reportGroupName;

    @Column(name = "REPORT_GROUP_NAME_EN", length = 256)
    private String reportGroupNameEn;

    @Column(name="SORT_NUMBER")
    private BigInteger sortNumber;

    public CtgCfgReportGroup(String reportGroupCode,BigInteger sortNumber, String reportGroupName,String reportGroupNameEn, String description) {
        this.reportGroupCode = reportGroupCode;
        this.reportGroupName = reportGroupName;
        this.reportGroupNameEn=reportGroupNameEn;
        this.sortNumber=sortNumber;
        this.setDescription(description);
        this.setApprovedBy(getCurrentUsername());
        this.setApprovedDate(getTimestampNow());
        this.setIsActive(EntityStatus.IsActive.ACTIVE.getValue());
        this.setRecordStatus(EntityStatus.RecordStatus.APPROVAL.getValue());

    }

}
