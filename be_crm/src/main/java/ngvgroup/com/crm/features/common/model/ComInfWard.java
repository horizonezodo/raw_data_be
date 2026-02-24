package ngvgroup.com.crm.features.common.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "COM_INF_WARD")
@Getter
@Setter
@NoArgsConstructor
public class ComInfWard extends BaseEntity {
    @Column(name = "WARD_CODE", nullable = false, length = 128)
    private String wardCode;

    @Column(name = "WARD_NAME", nullable = false)
    private String wardName;

    @Column(name = "DISTRICT_CODE", length = 128, nullable = false)
    private String districtCode;

    @Column(name = "PROVINCE_CODE", nullable = false, length = 128)
    private String provinceCode;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}