package ngvgroup.com.loan.feature.product_proccess.model.txn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.loan.feature.product_proccess.model.base.BaseLnmProductDtl;

@Entity
@Table(name = "LNM_TXN_PRODUCT_DTL")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class LnmTxnProductDtl extends BaseLnmProductDtl {
    @Column(name = "PROCESS_INSTANCE_CODE", nullable = false, length = 128)
    private String processInstanceCode;
}