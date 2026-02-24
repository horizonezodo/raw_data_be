package ngvgroup.com.rpt.features.report.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import ngvgroup.com.rpt.core.constant.VariableConstants;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CTG_CFG_REPORT_GROUP")
public class CtgCfgReportGroup extends BaseEntity {

    @Column(name = "REPORT_GROUP_CODE", nullable = false, length = 128)
    private String reportGroupCode;

    @Column(name = "REPORT_GROUP_NAME", nullable = false, length = 256)
    private String reportGroupName;

    @Column(name = "REPORT_GROUP_NAME_EN", length = 256)
    private String reportGroupNameEn;

    @Column(name="SORT_NUMBER")
    private BigInteger sortNumber;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

    public CtgCfgReportGroup(String reportGroupCode,BigInteger sortNumber, String reportGroupName,String reportGroupNameEn, String description) {
        this.reportGroupCode = reportGroupCode;
        this.reportGroupName = reportGroupName;
        this.reportGroupNameEn=reportGroupNameEn;
        this.sortNumber=sortNumber;
        this.setDescription(description);
        this.setRecordStatus(VariableConstants.DD);

    }

}
