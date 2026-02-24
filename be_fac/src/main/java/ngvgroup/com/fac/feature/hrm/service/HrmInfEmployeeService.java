package ngvgroup.com.fac.feature.hrm.service;

import ngvgroup.com.fac.feature.hrm.dto.ObjectTxnDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HrmInfEmployeeService {
    Page<ObjectTxnDto> getList(Pageable pageable);
}
