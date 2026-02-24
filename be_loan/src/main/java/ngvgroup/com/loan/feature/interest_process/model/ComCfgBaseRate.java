package ngvgroup.com.loan.feature.interest_process.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "COM_CFG_BASE_RATE")
@AllArgsConstructor
@NoArgsConstructor
public class ComCfgBaseRate extends BaseEntity {

    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "CURRENCY_CODE", length = 4)
    private String currencyCode;

    @Column(name = "BASE_RATE_CODE", length = 64)
    private String baseRateCode;

    @Column(name = "BASE_RATE_NAME", length = 256)
    private String baseRateName;

    @Column(name = "SOURCE_CODE", length = 32, nullable = false)
    private String sourceCode;

}