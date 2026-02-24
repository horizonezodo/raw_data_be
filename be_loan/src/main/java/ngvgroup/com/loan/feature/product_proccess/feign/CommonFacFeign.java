package ngvgroup.com.loan.feature.product_proccess.feign;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import ngvgroup.com.loan.feature.product_proccess.dto.FacCfgAccClassDto;
import ngvgroup.com.loan.feature.product_proccess.dto.FacCfgAccStructureDto;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "CommonFacFeign", url = "${service.fac.url}")
public interface CommonFacFeign {

    @GetMapping("/api/class/search")
    ResponseEntity<ResponseData<Page<FacCfgAccClassDto>>> searchAll(@RequestParam(required = false) String keyword, @ParameterObject Pageable pageable);

    @GetMapping("/api/acc-structure/list-structure")
    ResponseEntity<ResponseData<Page<FacCfgAccStructureDto>>> getAccStructure(Pageable pageable);
}
