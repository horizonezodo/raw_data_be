package ngvgroup.com.hrm.feature.hrm_cfg_status.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.hrm.feature.hrm_cfg_status.dto.HrmCfgStatusOptionDto;
import ngvgroup.com.hrm.feature.hrm_cfg_status.service.HrmCfgStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cfg-status")
@RequiredArgsConstructor
public class HrmCfgStatusController {
    private final HrmCfgStatusService hrmCfgStatusService;

    @Operation(summary = "Danh sách trạng thái")
    @GetMapping("/options")
    public ResponseEntity<ResponseData<List<HrmCfgStatusOptionDto>>> getOptionStatus() {
        List<HrmCfgStatusOptionDto> statuses = hrmCfgStatusService.listOptions();
        return ResponseData.okEntity(statuses);
    }
}
