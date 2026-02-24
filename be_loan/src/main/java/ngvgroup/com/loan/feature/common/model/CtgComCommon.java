package ngvgroup.com.loan.feature.common.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "CTG_COM_COMMON")
public class CtgComCommon extends BaseEntity {

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

    @Column(name = "SORT_NUMBER")
    private Integer sortNumber;

    @Column(name = "CONTROL_TYPE", length = 64)
    private String controlType;

    @Column(name = "SYS_MAP_CODE", length = 128)
    private String sysMapCode;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
