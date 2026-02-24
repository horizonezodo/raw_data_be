package ngvgroup.com.hrm.feature.employee.model.txn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ngvgroup.com.hrm.feature.employee.model.base.HrmBaseEmployee;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "HRM_TXN_EMPLOYEE")
public class HrmTxnEmployee extends HrmBaseEmployee {

    /** Mã loại quy trình */
    @Column(name = "PROCESS_TYPE_CODE", length = 128, nullable = false)
    private String processTypeCode;

    /** Mã giao dịch quy trình */
    @Column(name = "PROCESS_INSTANCE_CODE", length = 128, nullable = false)
    private String processInstanceCode;

    /** Ngày giao dịch */
    @Column(name = "TXN_DATE")
    private LocalDate txnDate;

    @Column(name = "BUSINESS_STATUS", nullable = false)
    private String businessStatus = "ACTIVE";

}