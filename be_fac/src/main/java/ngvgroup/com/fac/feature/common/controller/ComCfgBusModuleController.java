package ngvgroup.com.fac.feature.common.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.AllArgsConstructor;
import ngvgroup.com.fac.feature.common.dto.BusModuleProcessTypeCodeDto;
import ngvgroup.com.fac.feature.common.dto.ComCfgBusModuleDto;
import ngvgroup.com.fac.feature.common.dto.ModuleCodeTreeFilter;
import ngvgroup.com.fac.feature.common.service.ComCfgBusModuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bus-module")
@AllArgsConstructor
public class ComCfgBusModuleController {
    private final ComCfgBusModuleService comCfgBusModuleService;

    @GetMapping("/get-all")
    ResponseEntity<ResponseData<List<ComCfgBusModuleDto>>> getBusModules(){
        return ResponseData.okEntity(comCfgBusModuleService.getCtgCfgBusModules());
    }

    @GetMapping("/get-by-process-type-code")
    ResponseEntity<ResponseData<List<BusModuleProcessTypeCodeDto>>> getBusModulesByProcessTypeCode(
            @RequestParam(required = false) String processTypeCode){
        return ResponseData.okEntity(comCfgBusModuleService.getBusModulesByProcessTypeCode(processTypeCode));
    }

    @GetMapping("/get-tree-data")
    ResponseEntity<ResponseData<List<ModuleCodeTreeFilter>>> getTreeList(){
        return ResponseData.okEntity(comCfgBusModuleService.getTreeList());
    }
}
