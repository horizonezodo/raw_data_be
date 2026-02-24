package ngvgroup.com.hrm.feature.employee.repository;

import ngvgroup.com.hrm.feature.employee.model.audit.HrmInfEmployeeFamilyA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HrmInfEmployeeFamilyARepository extends JpaRepository<HrmInfEmployeeFamilyA, Long> {
}
