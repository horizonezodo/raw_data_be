package ngvgroup.com.rpt.features.ctgcfgstattemplate.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.rpt.core.annotation.FieldDisplayName;
import ngvgroup.com.rpt.features.common.model.ComCfgCommon;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CTG_CFG_STAT_REGULATORY")
public class CtgCfgStatRegulatory extends BaseEntity {
    @Column(name = "ORG_CODE", length = 64)
    @FieldDisplayName("Mã tổ chức")
    private String orgCode;

    @Column(name = "STAT_REGULATORY_CODE", length = 64, nullable = false)
    @FieldDisplayName("Mã thống kê quy định")
    private String statRegulatoryCode;

    @Column(name = "STAT_REGULATORY_NAME", length = 256, nullable = false)
    @FieldDisplayName("Tên mã thống kê quy định")
    private String statRegulatoryName;

    @Column(name = "STAT_TYPE_CODE", length = 64, nullable = false)
    @FieldDisplayName("Loại thống kê")
    private String statTypeCode;

    @Column(name = "REPORT_MODULE_CODE", length = 64, nullable = false)
    @FieldDisplayName("Mã phân hệ báo cáo")
    private String reportModuleCode;

    @Column(name = "SORT_NUMBER")
    @FieldDisplayName("Thứ tự sắp sếp")
    private Long sortNumber;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STAT_TYPE_CODE", referencedColumnName = "STAT_TYPE_CODE", insertable = false, updatable = false)
    private CtgCfgStatType statType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_MODULE_CODE", referencedColumnName = "COMMON_CODE", insertable = false, updatable = false)
    private ComCfgCommon common;
}
