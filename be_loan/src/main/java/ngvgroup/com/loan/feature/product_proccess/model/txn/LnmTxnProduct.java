package ngvgroup.com.loan.feature.product_proccess.model.txn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.loan.feature.product_proccess.model.base.BaseLnmProduct;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "LNM_TXN_PRODUCT")
public class LnmTxnProduct extends BaseLnmProduct {

    @Column(name = "PROCESS_TYPE_CODE", length = 128, nullable = false)
    private String processTypeCode;

    @Column(name = "PROCESS_INSTANCE_CODE", length = 128, nullable = false)
    private String processInstanceCode;

    @Column(name = "BUSINESS_STATUS", length = 32, nullable = false)
    private String businessStatus;
}
