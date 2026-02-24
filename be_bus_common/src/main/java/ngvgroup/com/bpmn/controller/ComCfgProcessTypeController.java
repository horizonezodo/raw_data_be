package ngvgroup.com.bpmn.controller;

import com.ngvgroup.bpm.core.dto.ResponseData;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpmn.dto.ComCfgProcessType.ComCfgProcessTypeDto;
import ngvgroup.com.bpmn.service.ComCfgProcessTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cfg-process-type")
@RequiredArgsConstructor
public class ComCfgProcessTypeController {

    private final ComCfgProcessTypeService cfgProcessTypeService;

    @GetMapping
    ResponseEntity<ResponseData<List<ComCfgProcessTypeDto>>> getListProcessType() {
        return ResponseData.okEntity(cfgProcessTypeService.getListProcessType());
    }

    @GetMapping("/get-by-group-code")
    ResponseEntity<ResponseData<List<ComCfgProcessTypeDto>>> getListProcessTypeByGroupCode(@RequestParam String processGroupCode) {
        return ResponseData.okEntity(cfgProcessTypeService.getListProcessTypeByGroupCode(processGroupCode));
    }
}
