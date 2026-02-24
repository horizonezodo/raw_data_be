package ngvgroup.com.loan.feature.type_of_capital_use.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LNM_CFG_CAPITAL_USE")
@Entity
public class LnmCfgCapitalUse extends BaseEntity {
    @Column(name = "ORG_CODE", length = 64 , nullable = false)
    private String orgCode;
    @Column(name = "CAPITAL_USE_CODE", length = 64 , nullable = false)
    private String capitalUseCode;
    @Column(name = "CAPITAL_USE_NAME", length = 256 , nullable = false)
    private String capitalUseName;
    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus;
}
