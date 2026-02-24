package ngvgroup.com.hrm.feature.employee.repository;

import ngvgroup.com.hrm.feature.employee.model.audit.HrmInfEmployeePositionA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HrmInfEmployeePositionARepository extends JpaRepository<HrmInfEmployeePositionA, Long> {
}
