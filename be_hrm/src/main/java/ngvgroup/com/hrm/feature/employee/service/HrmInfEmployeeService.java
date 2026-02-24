package ngvgroup.com.hrm.feature.employee.service;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.hrm.feature.employee.dto.HrmInfEmployeeListDTO;
import ngvgroup.com.hrm.feature.employee.dto.HrmInfEmployeeSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface HrmInfEmployeeService {
    Page<HrmInfEmployeeListDTO> search(HrmInfEmployeeSearchRequest request, Pageable pageable);

    ResponseEntity<byte[]> exportExcel(String fileName, HrmInfEmployeeSearchRequest request) throws BusinessException;
}
