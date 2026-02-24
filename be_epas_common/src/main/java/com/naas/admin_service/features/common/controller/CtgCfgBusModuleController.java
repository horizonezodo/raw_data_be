package com.naas.admin_service.features.common.controller;

import com.naas.admin_service.features.common.dto.ComCfgBusModuleDto;
import com.naas.admin_service.features.common.service.ComCfgBusModuleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import java.util.List;

@RestController
@RequestMapping("/bus-module")
@AllArgsConstructor
public class CtgCfgBusModuleController {
    private final ComCfgBusModuleService comCfgBusModuleService;

    @GetMapping("/get-all")
    ResponseEntity<ResponseData<List<ComCfgBusModuleDto>>> getBusModules(){
        return ResponseData.okEntity(comCfgBusModuleService.getCtgCfgBusModules());
    }
}
