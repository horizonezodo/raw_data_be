package ngvgroup.com.hrm.feature.employee.model.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ngvgroup.com.hrm.feature.employee.model.base.HrmBaseEmployeeFamily;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "HRM_INF_EMPLOYEE_FAMILY_A")
public class HrmInfEmployeeFamilyA extends HrmBaseEmployeeFamily {

    /** Mã giao dịch quy trình */
    @Column(name = "DATA_TIME")
    private LocalDateTime dataTime;

}