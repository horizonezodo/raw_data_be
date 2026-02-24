package ngvgroup.com.bpmn.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ngvgroup.bpm.core.dto.ResponseData;

import lombok.AllArgsConstructor;
import ngvgroup.com.bpmn.dto.CtgCfgResourceMapping.ResourceMappingDto;
import ngvgroup.com.bpmn.service.CtgCfgResourceMappingService;

@RestController
@RequestMapping("/com-cfg-resource-mapping")
@AllArgsConstructor
public class CtgCfgResourceMappingController {
    private final CtgCfgResourceMappingService ctgCfgResourceMappingService;

    @GetMapping("/list-current-branch")
    public ResponseEntity<ResponseData<List<ResourceMappingDto>>> getListCurrentBranch() {
        return ResponseEntity.ok(ctgCfgResourceMappingService.getListCurrentBranch());
    }
}
