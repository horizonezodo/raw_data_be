package ngvgroup.com.crm.features.customer.model.inf;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ngvgroup.com.crm.features.customer.model.base.BaseCust;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "CRM_INF_CUST")
public class CrmInfCust extends BaseCust {
}