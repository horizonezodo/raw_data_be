package ngvgroup.com.rpt.features.dim.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.dim.dto.DimCiDDTO;
import ngvgroup.com.rpt.features.dim.repository.DimCiDRepository;
import ngvgroup.com.rpt.features.dim.service.DimCiDService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DimCiDServiceImpl implements DimCiDService {
    private final DimCiDRepository repo;

    @Override
    public List<DimCiDDTO> listDim() {
        return repo.getAllData();
    }
}
