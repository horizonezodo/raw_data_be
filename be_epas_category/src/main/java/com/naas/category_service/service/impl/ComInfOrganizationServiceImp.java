package com.naas.category_service.service.impl;

import com.naas.category_service.dto.ComInfOrganizationDto;
import com.naas.category_service.repository.ComInfOrganizationRepository;
import com.naas.category_service.service.ComInfOrganizationService;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ComInfOrganizationServiceImp implements ComInfOrganizationService {
    private final ComInfOrganizationRepository comInfOrganizationRepository;

    @Override
    public List<ComInfOrganizationDto> listOrganizations() {
        return comInfOrganizationRepository.findAll().stream()
                .map(org -> new ComInfOrganizationDto(org.getOrgCode(), org.getOrgName()))
                .toList();
    }
}
