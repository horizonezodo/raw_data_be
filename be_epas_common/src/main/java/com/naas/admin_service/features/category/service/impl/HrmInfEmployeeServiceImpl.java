package com.naas.admin_service.features.category.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.naas.admin_service.features.category.dto.HrmInfEmployeeDTO;
import com.naas.admin_service.features.category.mapper.HrmInfEmployeeMapper;
import com.naas.admin_service.features.category.model.HrmInfEmployee;
import com.naas.admin_service.features.category.repository.HrmInfEmployeeRepository;
import com.naas.admin_service.features.category.service.HrmInfEmployeeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HrmInfEmployeeServiceImpl implements HrmInfEmployeeService {
    private final HrmInfEmployeeRepository repo;
    private final HrmInfEmployeeMapper mapper;

    @Override
    public List<HrmInfEmployeeDTO> listEmp() {
        List<HrmInfEmployee> lst = repo.findAll();
        return mapper.toListDto(lst);
    }
}
