package ngvgroup.com.hrm.feature.employee.repository;

import ngvgroup.com.hrm.feature.employee.model.txn.HrmTxnEmployeePosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HrmTxnEmployeePositionRepository extends JpaRepository<HrmTxnEmployeePosition, Long> {
    List<HrmTxnEmployeePosition> findByProcessInstanceCode(String processInstanceCode);

    @Modifying
    @Transactional
    void deleteByProcessInstanceCode(String processInstanceCode);
}
