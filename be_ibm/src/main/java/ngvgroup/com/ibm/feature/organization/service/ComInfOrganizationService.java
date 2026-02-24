package ngvgroup.com.ibm.feature.organization.service;

import ngvgroup.com.ibm.feature.organization.dto.ComInfOrganizationDto;

import java.util.List;

public interface ComInfOrganizationService {
    List<ComInfOrganizationDto> listOrganizations();

    List<ComInfOrganizationDto> searchOrganizations(String keyword);
}
