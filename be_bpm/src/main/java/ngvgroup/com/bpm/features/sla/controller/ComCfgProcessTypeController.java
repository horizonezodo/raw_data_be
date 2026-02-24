package ngvgroup.com.bpm.features.sla.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import ngvgroup.com.bpm.features.sla.dto.CtgCfgProcessTypeDto;
import ngvgroup.com.bpm.features.sla.service.ComCfgProcessTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cfg-process-type")
@AllArgsConstructor
public class ComCfgProcessTypeController {
    private final ComCfgProcessTypeService service;

    @Operation(
            summary = "Danh sách loại nghiệp vụ"
    )
    @GetMapping
    public ResponseEntity<ResponseData<List<CtgCfgProcessTypeDto>>> get() {
        List<CtgCfgProcessTypeDto> list = service.getAll();
        return ResponseData.okEntity(list);
    }

}
