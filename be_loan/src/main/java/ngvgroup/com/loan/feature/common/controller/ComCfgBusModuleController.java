package ngvgroup.com.loan.feature.common.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.feature.common.dto.ComCfgBusModuleDto;
import ngvgroup.com.loan.feature.common.service.ComCfgBusModuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bus-module")
@RequiredArgsConstructor
public class ComCfgBusModuleController {

    private final ComCfgBusModuleService comCfgBusModuleService;

    @GetMapping("/get-all")
    public ResponseEntity<ResponseData<List<ComCfgBusModuleDto>>> getAllBusModule() {
        return ResponseData.okEntity(comCfgBusModuleService.getALlCtgCfgBusModule());
    }
}
