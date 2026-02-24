package ngvgroup.com.bpmn.controller;

import com.ngvgroup.bpm.core.dto.ResponseData;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpmn.dto.CtgCfgProcessGroup.CtgCfgProcessGroupDto;
import ngvgroup.com.bpmn.service.CtgCfgProcessGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cfg-process-group")
@RequiredArgsConstructor
public class CtgCfgProcessGroupController {

    private final CtgCfgProcessGroupService ctgCfgProcessGroupService;
    @GetMapping
    ResponseEntity<ResponseData<List<CtgCfgProcessGroupDto>>> getListProcessGroup() {
        return ResponseData.okEntity(ctgCfgProcessGroupService.getListProcessGroup());
    }


}
