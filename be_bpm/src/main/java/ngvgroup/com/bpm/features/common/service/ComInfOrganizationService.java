package ngvgroup.com.bpm.features.common.service;

import ngvgroup.com.bpm.features.common.dto.ComInfOrganizationDto;

import java.util.List;

public interface ComInfOrganizationService {
    List<ComInfOrganizationDto> listOrganizations();

    List<ComInfOrganizationDto> searchOrganizations(String keyword);
}
