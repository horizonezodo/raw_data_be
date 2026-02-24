package com.naas.admin_service.features.category.controller;

import com.naas.admin_service.features.category.dto.CtgInfCurrencyTypeDto;
import com.naas.admin_service.features.category.service.CtgInfCurrencyTypeService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
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
