package ngvgroup.com.loan.feature.product_proccess.model.cfg;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.loan.feature.product_proccess.model.base.BaseLnmProductDtl;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "LNM_CFG_PRODUCT_DTL")
public class LnmCfgProductDtl extends BaseLnmProductDtl {
}
