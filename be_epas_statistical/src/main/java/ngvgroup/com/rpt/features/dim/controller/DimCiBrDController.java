package ngvgroup.com.rpt.features.dim.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.dim.dto.DimCiBrDDTO;
import ngvgroup.com.rpt.features.dim.service.DimCiBrDService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dim-ci-br")
public class DimCiBrDController {
    private final DimCiBrDService service;

    @LogActivity(function = "Lấy danh sách chi nhánh")
    @GetMapping
    public ResponseEntity<ResponseData<Page<DimCiBrDDTO>>> pageDimCiBr(@RequestParam String keyword,
                                                                       @RequestParam String scoreInstanceCode,
                                                                       @ParameterObject Pageable pageable){
        return ResponseData.okEntity(service.pageDimCiBr(keyword,scoreInstanceCode, pageable));
    }
}
