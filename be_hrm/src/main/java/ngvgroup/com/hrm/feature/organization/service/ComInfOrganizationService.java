package ngvgroup.com.hrm.feature.organization.service;

import ngvgroup.com.hrm.feature.organization.dto.ComInfOrganizationDto;

import java.util.List;

public interface ComInfOrganizationService {
    List<ComInfOrganizationDto> listOrganizations();

    List<ComInfOrganizationDto> searchOrganizations(String keyword);
}
