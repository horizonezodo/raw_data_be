package ngvgroup.com.hrm.feature.employee.service;

import ngvgroup.com.hrm.feature.employee.dto.EmployeeDetailDto;
import ngvgroup.com.hrm.feature.employee.dto.HrmAuthInfoSearchRequest;
import ngvgroup.com.hrm.feature.employee.dto.HrmAuthInfoSearchResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InfoService {
    Page<HrmAuthInfoSearchResponse> getEmployeeAuthSearch(HrmAuthInfoSearchRequest request, Pageable pageable);

    EmployeeDetailDto getProfile(String employeeCode);
}
