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
@Table(name = "COM_CFG_BASE_INT")
public class ComCfgBaseInt extends BaseEntity {

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive;

    @Column(name = "BASE_INT_CODE", length = 128)
    private String baseIntCode;

    @Column(name = "BASE_INT_NAME", nullable = false, length = 256)
    private String baseIntName;

    @Column(name = "INT_NUMR", nullable = false)
    private Integer intNumr;

    @Column(name = "INT_DNMR", nullable = false)
    private Integer intDnmr;
}
