package ngvgroup.com.hrm.feature.employee.model.inf;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.hrm.feature.employee.model.base.HrmBaseEmployeeAuth;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "HRM_INF_EMPLOYEE_AUTH")
public class HrmInfEmployeeAuth extends HrmBaseEmployeeAuth {

}