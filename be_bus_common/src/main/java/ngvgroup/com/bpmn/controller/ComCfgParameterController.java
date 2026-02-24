package ngvgroup.com.bpmn.controller;

import ngvgroup.com.bpmn.dto.ComCfgParameter.ComCfgParameterDto;
import ngvgroup.com.bpmn.service.ComCfgParameterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ngvgroup.bpm.core.dto.ResponseData;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/parameter")
public class ComCfgParameterController {

    private final ComCfgParameterService comCfgParameterService;

    public ComCfgParameterController(ComCfgParameterService comCfgParameterService) {
        this.comCfgParameterService = comCfgParameterService;
    }

    @Operation(summary = "Lấy thông tin parameter theo mã paramCode", description = "API lấy thông tin parameter theo mã paramCode từ query parameter")
    @GetMapping("/{paramCode}")
    public ResponseEntity<ResponseData<ComCfgParameterDto>> getParameterByCode(
            @PathVariable("paramCode") String paramCode) {
        ComCfgParameterDto response = comCfgParameterService.getParameterByCode(paramCode);
        return ResponseData.okEntity(response);
    }
}
