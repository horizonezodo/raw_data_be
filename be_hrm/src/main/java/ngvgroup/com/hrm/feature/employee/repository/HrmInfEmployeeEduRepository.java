package ngvgroup.com.hrm.feature.employee.repository;

import ngvgroup.com.hrm.feature.employee.model.inf.HrmInfEmployeeEdu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public interface HrmInfEmployeeEduRepository extends JpaRepository<HrmInfEmployeeEdu, Long> {

    @Modifying
    @Transactional
    void deleteByEmployeeCode(String employeeCode);

    List<HrmInfEmployeeEdu> findByEmployeeCode(String employeeCode);
}
