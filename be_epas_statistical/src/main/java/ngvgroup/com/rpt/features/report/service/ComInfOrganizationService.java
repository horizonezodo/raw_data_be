package ngvgroup.com.rpt.features.report.service;

import ngvgroup.com.rpt.features.report.dto.ComInfOrganizationDto;

import java.util.List;

public interface ComInfOrganizationService {
    List<ComInfOrganizationDto> listOrganizations();

    List<ComInfOrganizationDto> searchOrganizations(String keyword);
}
