package ngvgroup.com.hrm.feature.employee.model.txn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ngvgroup.com.hrm.feature.employee.model.base.HrmBaseEmployeeFamily;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "HRM_TXN_EMPLOYEE_FAMILY")
public class HrmTxnEmployeeFamily extends HrmBaseEmployeeFamily {

    /** Mã giao dịch quy trình */
    @Column(name = "PROCESS_INSTANCE_CODE", length = 128)
    private String processInstanceCode;

    /** Ngày giao dịch */
    @Column(name = "TXN_DATE")
    private LocalDate txnDate;

}