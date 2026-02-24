package com.naas.category_service.controller;

import com.naas.category_service.dto.CtgCfgBusModule.CtgCfgBusModuleDto;
import com.naas.category_service.dto.CtgInfCurrencyType.CtgInfCurrencyTypeDto;
import com.naas.category_service.service.CtgCfgBusModuleService;
import com.naas.category_service.service.CtgInfCurrencyTypeService;
import com.ngvgroup.bpm.core.dto.ResponseData;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/currency-type")
@AllArgsConstructor
public class CtgInfCurrencyTypeController {

    private final CtgInfCurrencyTypeService ctgInfCurrencyTypeService;

    @GetMapping("/get-all")
    ResponseEntity<ResponseData<List<CtgInfCurrencyTypeDto>>> getBusModules(){
        return ResponseData.okEntity(ctgInfCurrencyTypeService.getAllCtgInfCurrencyTypes());
    }
}
