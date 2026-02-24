package ngvgroup.com.bpmn.service.impl;

import lombok.AllArgsConstructor;
import ngvgroup.com.bpmn.dto.ComInfOrganization.ComInfOrganizationDto;
import ngvgroup.com.bpmn.repository.ComInfOrganizationRepository;
import ngvgroup.com.bpmn.service.ComInfOrganizationService;
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
