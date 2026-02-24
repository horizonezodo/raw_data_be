package ngvgroup.com.crm.features.customer.model.txn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ngvgroup.com.crm.features.customer.model.base.BaseCustKyc;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "CRM_TXN_CUST_KYC")
public class CrmTxnCustKyc extends BaseCustKyc {

    @Column(name = "TXN_DATE", nullable = false)
    private Date txnDate;

    @Column(name = "PROCESS_INSTANCE_CODE", length = 128, nullable = false)
    private String processInstanceCode;
}