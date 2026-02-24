package ngvgroup.com.fac.feature.hrm.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.fac.feature.hrm.dto.ObjectTxnDto;
import ngvgroup.com.fac.feature.hrm.model.HrmInfEmployee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HrmInfEmployeeRepository extends BaseRepository<HrmInfEmployee> {
    @Query("SELECT new ngvgroup.com.fac.feature.hrm.dto.ObjectTxnDto(" +
            "  e.employeeCode, " +
            "  e.employeeName, " +
            "  e.identificationId, " +
            "  e.currentAddress, " +
            "  e.issueDate, " +
            "  e.issuePlace) " +
            "FROM HrmInfEmployee e " +
            "WHERE e.isDelete = 0 ")
    Page<ObjectTxnDto> getTnxList(Pageable pageable);

    HrmInfEmployee findByEmployeeTypeCode(String employeeTypeCode);
}
