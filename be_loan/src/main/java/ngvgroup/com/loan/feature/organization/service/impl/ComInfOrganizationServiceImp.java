package ngvgroup.com.loan.feature.organization.service.impl;

import lombok.AllArgsConstructor;
import ngvgroup.com.loan.feature.organization.dto.ComInfOrganizationDto;
import ngvgroup.com.loan.feature.organization.repository.ComInfOrganizationRepository;
import ngvgroup.com.loan.feature.organization.service.ComInfOrganizationService;
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
