package ngvgroup.com.crm.features.common.controller;

import java.util.List;

import ngvgroup.com.crm.features.common.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ngvgroup.bpm.core.common.dto.ResponseData;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.crm.features.common.service.CommonService;

@RequestMapping("/crm-common")
@RestController
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;


    @GetMapping("/commons")
    public ResponseEntity<ResponseData<List<ComCfgCommonDto>>> getCommons(@RequestParam String commonTypeCode) {
        List<ComCfgCommonDto> result = commonService.getCommonConfigsByType(commonTypeCode);
        return ResponseData.okEntity(result);
    }

    @GetMapping("/provinces")
    public ResponseEntity<ResponseData<List<ComInfProvinceDto>>> getProvinces() {
        List<ComInfProvinceDto> result = commonService.getAllProvinces();
        return ResponseData.okEntity(result);
    }

    @GetMapping("/wards")
    public ResponseEntity<ResponseData<List<ComInfWardDto>>> getWards(@RequestParam String provinceCode) {
        List<ComInfWardDto> result = commonService.getWardsByProvince(provinceCode);
        return ResponseData.okEntity(result);
    }

    @GetMapping("/areas")
    public ResponseEntity<ResponseData<List<ComInfAreaDto>>> getAreas(@RequestParam(required = false) String orgCode) {
        List<ComInfAreaDto> result = commonService.getAreasByOrg(orgCode);
        return ResponseData.okEntity(result);
    }

    @GetMapping("/economic-types")
    public ResponseEntity<ResponseData<List<ComInfEconomicTypeDto>>> getEconomicTypes() {
        List<ComInfEconomicTypeDto> result = commonService.getAllEconomicTypes();
        return ResponseData.okEntity(result);
    }

    @GetMapping("/industries")
    public ResponseEntity<ResponseData<List<ComInfIndustryDto>>> getIndustries() {
        List<ComInfIndustryDto> result = commonService.getAllIndustries();
        return ResponseData.okEntity(result);
    }

    @GetMapping("/organizations")
    public ResponseEntity<ResponseData<List<ComInfOrganizationDto>>> getOrganizations() {
        List<ComInfOrganizationDto> result = commonService.getAllOrganizations();
        return ResponseData.okEntity(result);
    }

    @GetMapping("/commons-by-parent")
    public ResponseEntity<ResponseData<List<ComCfgCommonDto>>> getCommonsByParent(
            @RequestParam String commonTypeCode,
            @RequestParam String parentCode) {
        List<ComCfgCommonDto> result = commonService.getCommonConfigsByTypeAndParentCode(commonTypeCode, parentCode);
        return ResponseData.okEntity(result);
    }

    @GetMapping("/templates")
    public ResponseEntity<ResponseData<Page<TemplateResDto>>> searchAllTemplate(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        return ResponseData.okEntity(commonService.searchAllTemplate(keyword, pageable));
    }

    @GetMapping("/wards-by-org")
    public ResponseEntity<ResponseData<List<ComInfWardDto>>> getWardsByOrg(@RequestParam(required = false) String orgCode) {
        List<ComInfWardDto> result = commonService.getWardsByOrg(orgCode);
        return ResponseData.okEntity(result);
    }

    // 2. Get areas by ward
    @GetMapping("/areas-by-ward")
    public ResponseEntity<ResponseData<List<ComInfAreaDto>>> getAreasByWard() {
        List<ComInfAreaDto> result = commonService.getAreasByWard();
        return ResponseData.okEntity(result);
    }
}