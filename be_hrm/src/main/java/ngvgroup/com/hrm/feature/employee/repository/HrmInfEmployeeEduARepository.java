package ngvgroup.com.hrm.feature.employee.repository;

import ngvgroup.com.hrm.feature.employee.model.audit.HrmInfEmployeeEduA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HrmInfEmployeeEduARepository extends JpaRepository<HrmInfEmployeeEduA, Long> {
}
