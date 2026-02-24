package ngvgroup.com.bpmn.service;

import ngvgroup.com.bpmn.dto.ComInfOrganization.ComInfOrganizationDto;

import java.util.List;

public interface ComInfOrganizationService {
    List<ComInfOrganizationDto> listOrganizations();

    List<ComInfOrganizationDto> searchOrganizations(String keyword);
}
