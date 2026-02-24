package ngvgroup.com.rpt.features.ctgcfgresource.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgresource.dto.CtgCfgResourceMappingDTO;
import ngvgroup.com.rpt.features.ctgcfgresource.dto.ResourceMappingDto;
import ngvgroup.com.rpt.features.ctgcfgresource.service.CtgCfgResourceMappingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("resource-mapping")
public class CtgCfgResourceMappingController {
    private final CtgCfgResourceMappingService service;

    @LogActivity(function = "Lấy tất cả dữ liệu mapping resource")
    @GetMapping
    public ResponseEntity<ResponseData<List<CtgCfgResourceMappingDTO>>> getAllData(@RequestParam String resourceTypeCode){
        return ResponseData.okEntity(service.getAllData(resourceTypeCode));
    }

    @LogActivity(function = "Lấy danh sách chi nhánh hiện tại")
    @GetMapping("/list-current-branch")
    public ResponseEntity<ResponseData<List<ResourceMappingDto>>> getListCurrentBranch() {
        return ResponseEntity.ok(service.getListCurrentBranch());
    }

}
