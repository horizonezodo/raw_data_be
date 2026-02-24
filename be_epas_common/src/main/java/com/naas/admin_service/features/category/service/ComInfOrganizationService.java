package com.naas.admin_service.features.category.service;

import java.util.List;

import com.naas.admin_service.features.category.dto.cominforganization.ComInfOrganizationDto;

public interface ComInfOrganizationService {
    List<ComInfOrganizationDto> listOrganizations();

    List<ComInfOrganizationDto> searchOrganizations(String keyword);
}
