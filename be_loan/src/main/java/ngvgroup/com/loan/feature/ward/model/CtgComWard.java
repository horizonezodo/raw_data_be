package ngvgroup.com.loan.feature.ward.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "CTG_INF_WARD")
public class CtgComWard extends BaseEntity {

    @Column(name = "WARD_NAME", nullable = false, length = 256)
    private String wardName;

    @Column(name = "WARD_CODE", nullable = false, length = 128)
    private String wardCode;

    @Column(name = "DISTRICT_CODE", nullable = false, length = 128)
    private String districtCode;

    @Column(name = "PROVINCE_CODE", nullable = false, length = 128)
    private String provinceCode;
}
