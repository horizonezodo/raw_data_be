package ngvgroup.com.loan.feature.product_proccess.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import ngvgroup.com.loan.feature.product_proccess.dto.*;
import ngvgroup.com.loan.feature.product_proccess.service.ProductInfoService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("product-info")
public class ProductInfoController {

    private final ProductInfoService productInfoService;

    public ProductInfoController(ProductInfoService productInfoService) {
        this.productInfoService = productInfoService;
    }

    @PostMapping("/search")
    public ResponseEntity<ResponseData<Page<LnmCfgProductDTO>>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<String> commonCodes,
            @ParameterObject Pageable pageable) {
        return ResponseData.okEntity(productInfoService.search(keyword, commonCodes, pageable));
    }

    @GetMapping("/list-product-code")
    public ResponseEntity<ResponseData<List<String>>> getListProductCode() {
        return ResponseData.okEntity(productInfoService.getAllLnmProductCode());
    }

    @GetMapping("/get-acc-class")
    ResponseEntity<ResponseData<Page<FacCfgAccClassDto>>> searchAll(@RequestParam(required = false) String keyword, @ParameterObject Pageable pageable) {
        return ResponseData.okEntity(productInfoService.getListFacAccClass(keyword, pageable));
    }

    @GetMapping("/list-acc-structure")
    ResponseEntity<ResponseData<Page<FacCfgAccStructureDto>>> getAccStructure(Pageable pageable) {
        return ResponseData.okEntity(productInfoService.getListStructure(pageable));
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseData<ProductDetailDTO>> getProductDetail(@PathVariable Long id) {
        return ResponseData.okEntity(productInfoService.getDetailById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteProductDetail(
            @PathVariable Long id
    ) {
        productInfoService.deleteProduct(id);
        return ResponseData.okEntity();
    }

    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportExcel(
            @RequestBody List<LnmCfgProductDTO> list
    ) {
        return productInfoService.exportExcel(list);
    }

    @PostMapping("/export-excel/detail")
    public ResponseEntity<byte[]> exportDetail(
            @RequestBody List<LnmTxnProductDtlDTO> list
    ) {
        return productInfoService.exportDetail(list);
    }
}
