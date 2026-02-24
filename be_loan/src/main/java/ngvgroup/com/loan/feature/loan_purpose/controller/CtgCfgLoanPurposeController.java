package ngvgroup.com.loan.feature.loan_purpose.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.feature.loan_purpose.dto.CtgCfgLoanPurposeDto;
import ngvgroup.com.loan.feature.loan_purpose.dto.CtgCfgLoanPurposeResponse;
import ngvgroup.com.loan.feature.loan_purpose.service.CtgCfgLoanPurposeService;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ctg-cfg-loan-purpose")
@PreAuthorize("hasRole('category_product_loan_purpose')")
public class CtgCfgLoanPurposeController {
    private final CtgCfgLoanPurposeService ctgCfgLoanPurposeService;

    @PostMapping
    public ResponseEntity<ResponseData<Void>> createCtgCfgCollateralType(@RequestBody CtgCfgLoanPurposeDto request) {
        ctgCfgLoanPurposeService.create(request);
        return ResponseData.okEntity();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> updateCtgCfgCollateralType(@PathVariable("id") Long id,  @RequestBody CtgCfgLoanPurposeDto request) {
        ctgCfgLoanPurposeService.update(id, request);
        return ResponseData.okEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteCtgCfgCollateralType(@PathVariable("id") Long id) {
        ctgCfgLoanPurposeService.delete(id);
        return ResponseData.okEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<CtgCfgLoanPurposeDto>> getDetail(@PathVariable("id") Long id) {
        return ResponseData.okEntity(ctgCfgLoanPurposeService.findOne(id));
    }

    @GetMapping
    public ResponseEntity<ResponseData<Page<CtgCfgLoanPurposeResponse>>> search(@RequestParam(name = "filter",required = false) String filter,@ParameterObject Pageable pageable) {
        return ResponseData.okEntity(ctgCfgLoanPurposeService.searchAll(filter, pageable,false));
    }

    @GetMapping("/export-excel")
    public ResponseEntity<byte[]> exportToExcel(@RequestParam(name = "filter",required = false) String filter,

                                                           @RequestParam("labels") List<String> labels) {
        return ctgCfgLoanPurposeService.exportToExcel(filter, labels);
    }

    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam("purposeCode") String purposeCode) {
        return ResponseData.okEntity(ctgCfgLoanPurposeService.checkExist(purposeCode));
    }
}
