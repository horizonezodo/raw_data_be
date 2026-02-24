package ngvgroup.com.bpm.core.base.repository;

import ngvgroup.com.bpm.core.base.dto.HrmInfoDto;
import ngvgroup.com.bpm.core.base.model.HrmInfEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HrmInfEmployeeRepository extends JpaRepository<HrmInfEmployee, Long> {
    @Query("""
                SELECT new ngvgroup.com.bpm.core.base.dto.HrmInfoDto(
                    e.username,
                    e.employeeName,
                    cp.positionName
                    )
                FROM HrmInfEmployee e
                LEFT JOIN HrmInfEmployeePosition p ON p.employeeCode = e.employeeCode AND p.validFrom = (SELECT MAX(p2.validFrom) FROM HrmInfEmployeePosition p2 WHERE p2.employeeCode = e.employeeCode)
                LEFT JOIN HrmCfgPosition cp ON cp.positionCode = p.positionCode
                WHERE e.username IN :usernames
            """)
    List<HrmInfoDto> findByUsername(@Param("usernames") List<String> usernames);
}
