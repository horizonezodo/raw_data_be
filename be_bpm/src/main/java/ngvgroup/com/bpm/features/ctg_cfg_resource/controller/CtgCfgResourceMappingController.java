package ngvgroup.com.bpm.features.ctg_cfg_resource.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.AllArgsConstructor;
import ngvgroup.com.bpm.features.ctg_cfg_resource.dto.ResourceMappingDto;
import ngvgroup.com.bpm.features.ctg_cfg_resource.service.CtgCfgResourceMappingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/resource-mapping")
@AllArgsConstructor
public class CtgCfgResourceMappingController {
    private final CtgCfgResourceMappingService ctgCfgResourceMappingService;

    @GetMapping("/list-current-branch")
    public ResponseEntity<ResponseData<List<ResourceMappingDto>>> getListCurrentBranch() {
        return ResponseData.okEntity(ctgCfgResourceMappingService.getListCurrentBranch());
    }
}
