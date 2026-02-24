package ngvgroup.com.fac.feature.common.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Immutable;

@Table(name = "COM_CFG_COMMON")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Immutable
public class CtgCfgCommon extends BaseEntity {
    @Column(name = "COMMON_TYPE_CODE", length = 32, nullable = false)
    private String commonTypeCode;
    @Column(name = "COMMON_TYPE_NAME", length = 128, nullable = false)
    private String commonTypeName;
    @Column(name = "COMMON_CODE", length = 32)
    private String commonCode;
    @Column(name = "COMMON_NAME", length = 256)
    private String commonName;
    @Column(name = "COMMON_VALUE", length = 512)
    private String commonValue;
    @Column(name = "PARENT_CODE", length = 128)
    private String parentCode;
    @Column(name = "SYS_MAP_CODE", length = 128)
    private String sysMapCode;
    @Column(name = "SORT_NUMBER")
    private int sortNumber;
    @Column(name = "CONTROL_TYPE", length = 64)
    private String controlType;

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private Integer isActive = 1;
}
