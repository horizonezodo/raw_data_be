package ngvgroup.com.rpt.features.dim.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "DIM_CI_D")
public class DimCiD extends BaseEntity {
    @Column(name = "CI_ID",length = 128, nullable = false)
    private String ciId;
    @Column(name = "CI_CODE",length = 128, nullable = false)
    private String ciCode;
    @Column(name = "CI_NAME",length = 256, nullable = false)
    private String ciName;
    @Column(name = "CI_TYPE_CODE",length = 128)
    private String ciTypeCode;
    @Column(name = "PROVINCE_CODE",length = 128)
    private String provinceCode;
    @Column(name = "WARD_CODE",length = 128)
    private String wardCode;
    @Column(name = "PHONE_NUMBER",length = 128)
    private String phoneNumber;
    @Column(name = "EMAIL",length = 256)
    private String email;
    @Column(name = "TAX_CODE",length = 64)
    private String taxCode;
    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";
    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
