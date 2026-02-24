package com.naas.category_service.controller;

import com.naas.category_service.dto.CtgCfgBusModule.CtgCfgBusModuleDto;
import com.naas.category_service.service.CtgCfgBusModuleService;
import com.ngvgroup.bpm.core.dto.ResponseData;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bus-module")
@AllArgsConstructor
public class CtgCfgBusModuleController {
    private final CtgCfgBusModuleService ctgCfgBusModuleService;

    @GetMapping("/get-all")
    ResponseEntity<ResponseData<List<CtgCfgBusModuleDto>>> getBusModules(){
        return ResponseData.okEntity(ctgCfgBusModuleService.getCtgCfgBusModules());
    }
}
