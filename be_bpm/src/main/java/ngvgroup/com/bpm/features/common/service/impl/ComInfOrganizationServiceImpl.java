package ngvgroup.com.bpm.features.common.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpm.features.common.dto.ComInfOrganizationDto;
import ngvgroup.com.bpm.features.common.repository.ComInfOrganizationRepository;
import ngvgroup.com.bpm.features.common.service.ComInfOrganizationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComInfOrganizationServiceImpl implements ComInfOrganizationService {
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
