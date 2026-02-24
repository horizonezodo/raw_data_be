package ngvgroup.com.loan.feature.interest_rate.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.AllArgsConstructor;
import ngvgroup.com.loan.feature.interest_rate.dto.CtgCfgInterestRateDtlDetailDto;
import ngvgroup.com.loan.feature.interest_rate.dto.CtgCfgInterestRateDtlDto;
import ngvgroup.com.loan.feature.interest_rate.service.CtgCfgInterestRateDtlService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interest-rate-dtl")
@AllArgsConstructor
public class CtgCfgInterestRateDtlController {
    private CtgCfgInterestRateDtlService ctgCfgInterestRateDtlService;

    @PostMapping
    public ResponseEntity<ResponseData<Void>> create(@RequestBody List<CtgCfgInterestRateDtlDto> ctgCfgInterestRateDtlDtos ) {
        ctgCfgInterestRateDtlService.createInterestRateDtl(ctgCfgInterestRateDtlDtos);
        return ResponseData.createdEntity();
    }

    @PutMapping
    public ResponseEntity<ResponseData<Void>> update(@RequestBody List<CtgCfgInterestRateDtlDto> ctgCfgInterestRateDtlDtos){
        ctgCfgInterestRateDtlService.updateInterestRateDtl(ctgCfgInterestRateDtlDtos);
        return ResponseData.createdEntity();
    }

    @DeleteMapping
    public ResponseEntity<ResponseData<Void>> delete(@RequestParam("interestCode") String interestCode){
        ctgCfgInterestRateDtlService.deleteInterestRateDtl(interestCode);
        return ResponseData.okEntity();
    }

    @DeleteMapping("/delete-by-id")
    public ResponseEntity<ResponseData<Void>> deleteById(@RequestParam("id") Long id){
        ctgCfgInterestRateDtlService.deleteById(id);
        return ResponseData.okEntity();
    }

    @GetMapping("/get-detail")
    public ResponseEntity<ResponseData<Page<CtgCfgInterestRateDtlDetailDto>>> getDetail(@RequestParam(name="interestCode",required = false) String interestCode , @RequestParam(name = "keyword",required = false) String keyword, @ParameterObject Pageable pageable){
        return ResponseData.okEntity(ctgCfgInterestRateDtlService.getDetailInterestRateDtls(interestCode, keyword, pageable));
    }


    @GetMapping("/get-all")
    public  ResponseEntity<ResponseData<List<CtgCfgInterestRateDtlDetailDto>>> getAll(@RequestParam(name = "interestCode",required = false) String interestCode , @RequestParam(name = "keyword",required = false) String keyword){
        return ResponseData.okEntity(ctgCfgInterestRateDtlService.getAll(interestCode,keyword));
    }


}
