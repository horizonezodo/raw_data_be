package ngvgroup.com.ibm.feature.organization.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.ibm.feature.organization.dto.ComInfOrganizationDto;
import ngvgroup.com.ibm.feature.organization.repository.ComInfOrganizationRepository;
import ngvgroup.com.ibm.feature.organization.service.ComInfOrganizationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
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
