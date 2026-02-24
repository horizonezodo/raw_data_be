package ngvgroup.com.rpt.features.dim.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.dim.model.DimProvD;
import ngvgroup.com.rpt.features.dim.repository.DimProvDRepository;
import ngvgroup.com.rpt.features.dim.service.DimProvDService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DimProvDServiceImpl implements DimProvDService {
    private final DimProvDRepository dimProvDRepository;
    @Override
    public List<DimProvD> getAll(String ciId) {
        return dimProvDRepository.getAll(ciId);
    }
}
