package ngvgroup.com.hrm.feature.employee.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.hrm.feature.employee.dto.EmployeeDetailDto;
import ngvgroup.com.hrm.feature.employee.dto.HrmAuthInfoSearchRequest;
import ngvgroup.com.hrm.feature.employee.dto.HrmAuthInfoSearchResponse;
import ngvgroup.com.hrm.feature.employee.dto.HrmProfileDto;
import ngvgroup.com.hrm.feature.employee.service.InfoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("hrm")
@RequiredArgsConstructor
@RestController
public class HrmInfoController {
    private final InfoService service;

    @PostMapping("/auth-infos")
    public ResponseEntity<ResponseData<Page<HrmAuthInfoSearchResponse>>> getAuthInfos(
            @RequestBody HrmAuthInfoSearchRequest request, Pageable pageable) {
        return ResponseData.okEntity(service.getEmployeeAuthSearch(request, pageable));
    }

    @GetMapping("/profile/{employeeCode}")
    public ResponseEntity<ResponseData<EmployeeDetailDto>> getProfile(@PathVariable String employeeCode) {
        return ResponseData.okEntity(service.getProfile(employeeCode));
    }
}
