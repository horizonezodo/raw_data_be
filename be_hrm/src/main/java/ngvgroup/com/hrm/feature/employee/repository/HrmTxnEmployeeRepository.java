package ngvgroup.com.hrm.feature.employee.repository;

import ngvgroup.com.hrm.feature.employee.model.txn.HrmTxnEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HrmTxnEmployeeRepository extends JpaRepository<HrmTxnEmployee, Long> {
        Optional<HrmTxnEmployee> findByProcessInstanceCode(String processInstanceCode);

        boolean existsByMobileNumberAndIdNotAndBusinessStatusNot(String mobileNumber, Long id, String businessStatus);

        boolean existsByMobileNumberAndBusinessStatusNot(String mobileNumber, String businessStatus);

        boolean existsByIdentificationIdAndIdNotAndBusinessStatusNot(String identificationId, Long id,
                        String businessStatus);

        boolean existsByIdentificationIdAndBusinessStatusNot(String identificationId, String businessStatus);

        boolean existsByMobileNumberAndEmployeeCodeNotAndBusinessStatusNot(String mobileNumber, String employeeCode,
                        String businessStatus);

        boolean existsByIdentificationIdAndEmployeeCodeNotAndBusinessStatusNot(String identificationId,
                        String employeeCode,
                        String businessStatus);
}
