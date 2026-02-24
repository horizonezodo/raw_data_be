package ngvgroup.com.fac.feature.common.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.fac.feature.common.dto.ComInfOrganizationDTO;
import ngvgroup.com.fac.feature.common.repository.ComInfOrganizationRepository;
import ngvgroup.com.fac.feature.common.service.ComInfOrganizationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComInfOrganizationServiceImpl implements ComInfOrganizationService {
    private final ComInfOrganizationRepository repo;

    @Override
    public List<ComInfOrganizationDTO> getAll() {
        return this.repo.getAllOrganizations();
    }
}
