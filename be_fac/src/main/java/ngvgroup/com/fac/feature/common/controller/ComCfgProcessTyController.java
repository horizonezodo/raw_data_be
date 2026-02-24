package ngvgroup.com.fac.feature.common.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import ngvgroup.com.fac.feature.common.dto.ComCfgProcessTypeDTO;
import ngvgroup.com.fac.feature.common.service.ComCfgProcessTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/process-type")
public class ComCfgProcessTyController {
    private final ComCfgProcessTypeService cfgProcessTypeService;

    public ComCfgProcessTyController(ComCfgProcessTypeService cfgProcessTypeService) {
        this.cfgProcessTypeService = cfgProcessTypeService;
    }

    @GetMapping
    public ResponseEntity<ResponseData<List<ComCfgProcessTypeDTO>>> getAll() {
        return ResponseData.okEntity(cfgProcessTypeService.getAll());
    }
}
