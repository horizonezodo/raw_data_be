package ngvgroup.com.hrm.feature.employee.repository;

import ngvgroup.com.hrm.feature.employee.model.txn.HrmTxnEmployeeFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HrmTxnEmployeeFamilyRepository extends JpaRepository<HrmTxnEmployeeFamily, Long> {
    List<HrmTxnEmployeeFamily> findByProcessInstanceCode(String processInstanceCode);

    @Modifying
    @Transactional
    void deleteByProcessInstanceCode(String processInstanceCode);
}
