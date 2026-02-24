package ngvgroup.com.ibm.feature.dep_product.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.ibm.feature.dep_product.dto.IbmCfgDepProductDTO;
import ngvgroup.com.ibm.feature.dep_product.model.IbmCfgDepProduct;
import ngvgroup.com.ibm.feature.dep_product.service.IbmCfgDepProductService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dep-product")
@PreAuthorize("hasRole('category_ibm_dep_product')")
public class IbmCfgDepProductController extends BaseController<IbmCfgDepProduct, IbmCfgDepProductDTO, IbmCfgDepProductService> {

    public IbmCfgDepProductController(IbmCfgDepProductService service) {
        super(service);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseData<Page<IbmCfgDepProductDTO>>> search(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "") List<String> ibmDepProductTypeCode,
            @ParameterObject Pageable pageable
    ) {
        Page<IbmCfgDepProductDTO> result = service.search(keyword, ibmDepProductTypeCode, pageable);
        return ResponseData.okEntity(result);
    }

    @GetMapping("/detail/{productCode}")
    public ResponseEntity<ResponseData<IbmCfgDepProductDTO>> getDetail(@PathVariable String productCode) {
        IbmCfgDepProductDTO dto = service.getDetail(productCode);
        return ResponseData.okEntity(dto);
    }

}