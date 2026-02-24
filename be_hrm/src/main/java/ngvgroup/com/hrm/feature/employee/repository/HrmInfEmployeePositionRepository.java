package ngvgroup.com.hrm.feature.employee.repository;

import ngvgroup.com.hrm.feature.employee.model.inf.HrmInfEmployeePosition;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public interface HrmInfEmployeePositionRepository extends JpaRepository<HrmInfEmployeePosition, Long> {

    @Modifying
    @Transactional
    void deleteByEmployeeCode(String employeeCode);

    java.util.Optional<HrmInfEmployeePosition> findByEmployeeCodeAndPositionCode(String employeeCode,
            String positionCode);

    List<HrmInfEmployeePosition> findByEmployeeCode(String employeeCode);
}
