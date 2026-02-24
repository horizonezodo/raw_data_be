package ngvgroup.com.fac.feature.fac_inf_acc.model;

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
@Table(name = "COM_INF_CURRENCY_TYPE")
public class ComInfCurrencyType extends BaseEntity {
    @Column(name = "CURRENCY_CODE", length = 4)
    private String currencyCode;

    @Column(name = "CURRENCY_NAME", length = 128)
    private String currencyName;

    @Column(name = "SYMBOL", length = 32)
    private String symbol;

    @Column(name = "COUNTRY", length = 256)
    private String country;

    @Column(name = "DECIMAL_PLACES")
    private Integer decimalPlaces;
}
