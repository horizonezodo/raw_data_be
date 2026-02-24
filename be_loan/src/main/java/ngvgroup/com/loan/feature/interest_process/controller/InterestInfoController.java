package ngvgroup.com.loan.feature.interest_process.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.feature.interest_process.dto.InterestDetailDTO;
import ngvgroup.com.loan.feature.interest_process.dto.CfgInterestRateDTO;
import ngvgroup.com.loan.feature.interest_process.dto.LnmCfgIntRateDTO;
import ngvgroup.com.loan.feature.interest_process.service.InterestInfoService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interest-info")
@RequiredArgsConstructor
public class InterestInfoController {
    private final InterestInfoService service;
    @PostMapping("/search")
    public ResponseEntity<ResponseData<Page<LnmCfgIntRateDTO>>> search(@RequestParam String keyword,
                                                                       @RequestParam(required = false) List<String> commonCodes,
                                                                       @ParameterObject Pageable pageable) {
        return ResponseData.okEntity(service.search(keyword, commonCodes, pageable));
    }

    @GetMapping("/list-interest-rate-code")
    public ResponseEntity<ResponseData<List<String>>> getListInterestRateCode() {
        return ResponseData.okEntity(
                service.getAllInterestRateCode()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<InterestDetailDTO>> getInterestDetail(
            @PathVariable Long id
    ) {
        return ResponseData.okEntity(
                service.getInterestDetail(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteInterestDetail(
            @PathVariable Long id
    ) {
        service.deleteInterest(id);
        return ResponseData.okEntity();
    }



    @GetMapping("/interest-rate")
    public ResponseEntity<ResponseData<List<CfgInterestRateDTO>>> getCfgInterestDTO() {
        return ResponseData.okEntity(
                service.getListRate()
        );
    }

    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportExcel(
    ) {
        return service.exportExcel();
    }
}
