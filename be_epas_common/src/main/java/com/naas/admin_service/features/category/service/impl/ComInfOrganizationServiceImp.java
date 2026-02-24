package com.naas.admin_service.features.category.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.naas.admin_service.features.category.dto.cominforganization.ComInfOrganizationDto;
import com.naas.admin_service.features.category.repository.ComInfOrganizationRepository;
import com.naas.admin_service.features.category.service.ComInfOrganizationService;

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

    @Override
    public List<ComInfOrganizationDto> searchOrganizations(String keyword) {
        return comInfOrganizationRepository.listOrganization(keyword);
    }
}
