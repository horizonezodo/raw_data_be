package ngvgroup.com.rpt.features.ctgcfgstatruletype.model;

import jakarta.persistence.*;
import lombok.*;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@Entity
@Table(name = "CTG_CFG_STAT_RULE_TYPE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatRuleType extends BaseEntity {
    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode; // Mã tổ chức

    @Column(name = "RULE_TYPE_CODE", nullable = false, length = 64)
    private String ruleTypeCode; // Mã loại quy tắc

    @Column(name = "RULE_TYPE_NAME", nullable = false, length = 256)
    private String ruleTypeName; // Tên loại quy tắc

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
