package ngvgroup.com.rpt.features.common.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.jcip.annotations.Immutable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "COM_CFG_COMMON")
@Immutable
public class ComCfgCommon extends BaseEntity {
    @Column(name = "COMMON_TYPE_CODE", length = 32, nullable = false)
    private String commonTypeCode;

    @Column(name = "COMMON_TYPE_NAME", length = 128, nullable = false)
    private String commonTypeName;

    @Column(name = "COMMON_CODE", length = 32, nullable = false)
    private String commonCode;

    @Column(name = "COMMON_NAME", length = 256, nullable = false)
    private String commonName;

    @Column(name = "COMMON_VALUE", length = 64)
    private String commonValue;

    @Column(name = "PARENT_CODE", length = 128)
    private String parentCode;

    @Column(name = "SYS_MAP_CODE", length = 128)
    private String sysMapCode;

    @Column(name = "SORT_NUMBER", length = 22)
    private Integer sortNumber;

    @Column(name = "CONTROL_TYPE", length = 64)
    private String controlType;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
