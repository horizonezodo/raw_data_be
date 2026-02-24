package ngvgroup.com.rpt.features.report.service.impl;

import lombok.AllArgsConstructor;
import ngvgroup.com.rpt.features.report.dto.ComInfOrganizationDto;
import ngvgroup.com.rpt.features.report.repository.ComInfOrganizationRepository;
import ngvgroup.com.rpt.features.report.service.ComInfOrganizationService;
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

    @Override
    public List<ComInfOrganizationDto> searchOrganizations(String keyword) {
        return comInfOrganizationRepository.listOrganization(keyword);
    }
}
