package ngvgroup.com.hrm.feature.employee.model.inf;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.hrm.feature.employee.model.base.HrmBaseEmployeePosition;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "HRM_INF_EMPLOYEE_POSITION")
public class HrmInfEmployeePosition extends HrmBaseEmployeePosition {
}