package ngvgroup.com.crm.features.common.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COM_INF_ECONOMIC_TYPE")
public class ComInfEconomicType extends BaseEntity {
    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "ECONOMIC_TYPE_CODE", nullable = false, length = 64)
    private String economicTypeCode;

    @Column(name = "ECONOMIC_TYPE_NAME", nullable = false, length = 256)
    private String economicTypeName;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
