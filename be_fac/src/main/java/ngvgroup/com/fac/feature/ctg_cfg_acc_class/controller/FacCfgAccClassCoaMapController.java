package ngvgroup.com.fac.feature.ctg_cfg_acc_class.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;

import lombok.AllArgsConstructor;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.dto.FacCfgAccClassCoaMapDto;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.service.FacCfgAccClassCoaMapService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("acc-coa-map")
@AllArgsConstructor
public class FacCfgAccClassCoaMapController {
    private final FacCfgAccClassCoaMapService facCfgAccClassCoaMapService;

    @GetMapping("/search-all")
    ResponseEntity<ResponseData<Page<FacCfgAccClassCoaMapDto>>> searchAll(@RequestParam String keyword, @RequestParam String accClassCode, Pageable pageable){
        return ResponseData.okEntity(facCfgAccClassCoaMapService.searchAll(keyword,accClassCode,pageable));
    }

    @PostMapping
    ResponseEntity<ResponseData<Void>> create(@RequestBody List<FacCfgAccClassCoaMapDto> facCfgAccClassCoaMapDtos){
        facCfgAccClassCoaMapService.create(facCfgAccClassCoaMapDtos);
        return ResponseData.okEntity();
    }

    @PutMapping
    ResponseEntity<ResponseData<Void>> update(@RequestBody List<FacCfgAccClassCoaMapDto> facCfgAccClassCoaMapDtos,@RequestParam String accClassCode){
        facCfgAccClassCoaMapService.update(facCfgAccClassCoaMapDtos,accClassCode);
        return ResponseData.okEntity();
    }

    @GetMapping("/get-by-org-and-acc-class-code")
    ResponseEntity<ResponseData<FacCfgAccClassCoaMapDto>>getByOrgCodeAndAccClassCode(@RequestParam String orgCode,@RequestParam String accClassCode){
        return ResponseData.okEntity(facCfgAccClassCoaMapService.getByOrgCodeAndAccClassCode(orgCode,accClassCode));
    }

    @DeleteMapping
    ResponseEntity<ResponseData<Void>> delete(@RequestParam String accClassCode){
        facCfgAccClassCoaMapService.delete(accClassCode);
        return ResponseData.okEntity();
    }
}
