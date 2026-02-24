package ngvgroup.com.fac.feature.hrm.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.fac.feature.hrm.dto.ObjectTxnDto;
import ngvgroup.com.fac.feature.hrm.service.HrmInfEmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("hrm")
public class HrmInfEmployeeController {
    private final HrmInfEmployeeService service;

    @GetMapping
    public ResponseEntity<ResponseData<Page<ObjectTxnDto>>> getList(Pageable pageable) {
        return ResponseData.okEntity(service.getList(pageable));
    }
}
