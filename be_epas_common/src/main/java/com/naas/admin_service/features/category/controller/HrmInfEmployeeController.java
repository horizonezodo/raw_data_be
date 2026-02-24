package com.naas.admin_service.features.category.controller;

import com.naas.admin_service.features.category.dto.HrmInfEmployeeDTO;
import com.naas.admin_service.features.category.service.HrmInfEmployeeService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
public class HrmInfEmployeeController {
    private final HrmInfEmployeeService service;


    @GetMapping
    public ResponseEntity<ResponseData<List<HrmInfEmployeeDTO>>> listGroups() {
        List<HrmInfEmployeeDTO> lst = service.listEmp();
        return ResponseData.okEntity(lst);
    }

}
