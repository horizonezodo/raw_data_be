package ngvgroup.com.crm.features.customer.model.history;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ngvgroup.com.crm.features.customer.model.base.BaseCustSeg;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "CRM_INF_CUST_SEG_A")
public class CrmInfCustSegA extends BaseCustSeg {

    @Column(name = "DATA_TIME", nullable = false)
    private LocalDateTime dataTime;
}