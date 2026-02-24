package ngvgroup.com.loan.feature.interest_rate.controller;


import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.AllArgsConstructor;
import ngvgroup.com.loan.feature.interest_rate.dto.CtgCfgInterestRateDto;
import ngvgroup.com.loan.feature.interest_rate.service.CtgCfgInterestRateService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interest-rate")
@AllArgsConstructor
public class CtgCfgInterestRateController {
    private final CtgCfgInterestRateService ctgCfgInterestRateService;

    @GetMapping
    public ResponseEntity<ResponseData<Page<CtgCfgInterestRateDto>>> searchAll(@RequestParam("keyword") String keyword,
                                                                               @RequestParam(value = "orgCode",required = false) String orgCode,
                                                                               @RequestParam(value = "moduleCodes",required = false) List<String> moduleCodes,
                                                                               @ParameterObject Pageable pageable) {
        return ResponseData.okEntity(ctgCfgInterestRateService.searchAll(keyword,orgCode,moduleCodes, pageable));
    }

    @PostMapping("/export-to-excel/{fileName}")
    public ResponseEntity<byte[]> exportToExcel(@RequestParam("keyword") String keyword, @PathVariable("fileName") String fileName, @RequestBody List<String> label){
        return ctgCfgInterestRateService.exportToExcel(keyword,fileName,label);
    }

    @PostMapping
    public ResponseEntity<ResponseData<Void>> create(@RequestBody CtgCfgInterestRateDto ctgCfgInterestRateDto) {
        ctgCfgInterestRateService.createInterestRate(ctgCfgInterestRateDto);
        return ResponseData.createdEntity();
    }

    @PutMapping
    public ResponseEntity<ResponseData<Void>> update(@RequestBody CtgCfgInterestRateDto ctgCfgInterestRateDto) {
        ctgCfgInterestRateService.updateInterestRate(ctgCfgInterestRateDto);
        return ResponseData.createdEntity();
    }

    @DeleteMapping
    public ResponseEntity<ResponseData<Void>> delete(@RequestParam("interestCode") String interestCode) {
        ctgCfgInterestRateService.deleteInterestRate(interestCode);
        return ResponseData.okEntity();
    }

    @GetMapping("/get-detail")
    public ResponseEntity<ResponseData<CtgCfgInterestRateDto>> getDetail(@RequestParam("interestCode") String interestCode) {
        return ResponseData.okEntity(ctgCfgInterestRateService.getDetailInterestRate(interestCode));
    }

    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam("interestCode") String interestCode) {
        return ResponseData.okEntity(ctgCfgInterestRateService.checkExist(interestCode));
    }

}
