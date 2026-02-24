package ngvgroup.com.hrm.feature.employee.model.history;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ngvgroup.com.hrm.feature.employee.model.base.HrmBaseEmployeeEdu;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "HRM_INF_EMPLOYEE_EDU_H")
public class HrmInfEmployeeEduH extends HrmBaseEmployeeEdu {

    /** Mã giao dịch quy trình */
    @Column(name = "DATA_DATE")
    private LocalDate dataDate;

}