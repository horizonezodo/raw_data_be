package ngvgroup.com.rpt.features.ctgcfgstattemplate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import java.util.Date;

@Table(name = "CTG_CFG_STAT_REGULATORY_TYPE")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgStatRegulatoryType extends BaseEntity {
    @Column(name = "REGULATORY_TYPE_CODE", length = 64, nullable = false)
    private String regulatoryTypeCode;
    @Column(name = "REGULATORY_TYPE_NAME", length = 128, nullable = false)
    private String regulatoryTypeName;
    @Column(name = "ISSUED_BY", length = 512)
    private String issuedBy;
    @Column(name = "ISSUED_DATE")
    private Date issuedDate;
    @Column(name = "EFFECTIVE_DATE")
    private Date effectiveDate;
    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";
    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

}
