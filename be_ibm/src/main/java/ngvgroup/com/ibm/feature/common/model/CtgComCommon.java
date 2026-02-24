package ngvgroup.com.ibm.feature.common.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "COM_CFG_COMMON")
public class CtgComCommon extends BaseEntity {

    @Column(name = "COMMON_TYPE_CODE", nullable = false, length = 32)
    private String commonTypeCode;

    @Column(name = "COMMON_TYPE_NAME", nullable = false, length = 128)
    private String commonTypeName;

    @Column(name = "COMMON_CODE", nullable = false, length = 32)
    private String commonCode;

    @Column(name = "COMMON_NAME", nullable = false, length = 256)
    private String commonName;

    @Column(name = "COMMON_VALUE", length = 64)
    private String commonValue;

    @Column(name = "PARENT_CODE", length = 128)
    private String parentCode;

    @Column(name = "SORT_NUMBER")
    private Long sortNumber;

    @Column(name = "CONTROL_TYPE", length = 64)
    private String controlType;
}
