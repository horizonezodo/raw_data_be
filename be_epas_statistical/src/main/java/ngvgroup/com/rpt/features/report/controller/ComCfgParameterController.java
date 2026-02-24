package ngvgroup.com.rpt.features.report.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import io.swagger.v3.oas.annotations.Operation;
import ngvgroup.com.rpt.features.report.dto.ComCfgParameterDto;
import ngvgroup.com.rpt.features.report.service.ComCfgParameterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parameter")
public class ComCfgParameterController {

    private final ComCfgParameterService comCfgParameterService;

    public ComCfgParameterController(ComCfgParameterService comCfgParameterService) {
        this.comCfgParameterService = comCfgParameterService;
    }

    @LogActivity(function = "Lấy thông tin parameter")
    @Operation(summary = "Lấy thông tin parameter theo mã paramCode", description = "API lấy thông tin parameter theo mã paramCode từ query parameter")
    @GetMapping("/{paramCode}")
    public ResponseEntity<ResponseData<ComCfgParameterDto>> getParameterByCode(
            @PathVariable("paramCode") String paramCode){
        ComCfgParameterDto response = comCfgParameterService.getParameterByCode(paramCode);
        return ResponseData.okEntity(response);
    }
}
