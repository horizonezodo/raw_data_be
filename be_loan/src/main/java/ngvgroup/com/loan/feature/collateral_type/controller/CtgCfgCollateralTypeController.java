package ngvgroup.com.loan.feature.collateral_type.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.feature.collateral_type.dto.CtgCfgCollateralTypeDto;
import ngvgroup.com.loan.feature.collateral_type.dto.CtgCfgCollateralTypeResponse;
import ngvgroup.com.loan.feature.collateral_type.service.CtgCfgCollateralTypeService;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ctg-cfg-collateral-type")
@PreAuthorize("hasRole('category_product_collateral_type')")
public class CtgCfgCollateralTypeController {
    private final CtgCfgCollateralTypeService ctgCfgCollateralTypeService;

    @PostMapping
    public ResponseEntity<ResponseData<Void>> createCtgCfgCollateralType(@RequestBody CtgCfgCollateralTypeDto request) {
        ctgCfgCollateralTypeService.create(request);
        return ResponseData.okEntity();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> updateCtgCfgCollateralType(@PathVariable("id") Long id,  @RequestBody CtgCfgCollateralTypeDto request) {
        ctgCfgCollateralTypeService.update(id, request);
        return ResponseData.okEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteCtgCfgCollateralType(@PathVariable("id") Long id) {
        ctgCfgCollateralTypeService.delete(id);
        return ResponseData.okEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<CtgCfgCollateralTypeDto>> getDetail(@PathVariable("id") Long id) {
        return ResponseData.okEntity(ctgCfgCollateralTypeService.findOne(id));
    }

    @GetMapping
    public ResponseEntity<ResponseData<Page<CtgCfgCollateralTypeResponse>>> search(@RequestParam(name = "filter",required = false) String filter, @ParameterObject Pageable pageable) {
        return ResponseData.okEntity(ctgCfgCollateralTypeService.searchAll(filter, pageable, false));
    }

    @GetMapping("/export-excel")
    public ResponseEntity<byte[]> exportToExcel(@RequestParam(value = "filter",required = false) String filter,

                                                           @RequestParam List<String> labels) {
        return ctgCfgCollateralTypeService.exportToExcel(filter, labels);
    }

    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam("collateralTypeCode") String collateralTypeCode) {
        return ResponseData.okEntity(ctgCfgCollateralTypeService.checkExist(collateralTypeCode));
    }

}
