package ngvgroup.com.hrm.feature.employee.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.hrm.feature.employee.dto.HrmInfEmployeeListDTO;
import ngvgroup.com.hrm.feature.employee.dto.HrmInfEmployeeSearchRequest;
import ngvgroup.com.hrm.feature.employee.service.HrmInfEmployeeService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class HrmInfEmployeeController {
    private final HrmInfEmployeeService service;

    public HrmInfEmployeeController(HrmInfEmployeeService service) {
        this.service = service;
    }

    @PostMapping("/search")
    public ResponseEntity<ResponseData<Page<HrmInfEmployeeListDTO>>> search(
            @RequestBody HrmInfEmployeeSearchRequest request, @ParameterObject Pageable pageable) {
        Page<HrmInfEmployeeListDTO> page = service.search(request, pageable);
        return ResponseData.okEntity(page);
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> export(@RequestParam String fileName,
            @RequestBody HrmInfEmployeeSearchRequest request) throws BusinessException {
        return service.exportExcel(fileName, request);
    }
}
