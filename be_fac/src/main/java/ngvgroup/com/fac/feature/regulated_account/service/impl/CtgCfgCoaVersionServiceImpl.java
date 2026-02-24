package ngvgroup.com.fac.feature.regulated_account.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.fac.feature.regulated_account.dto.CtgCfgCoaVersionDTO;
import ngvgroup.com.fac.feature.regulated_account.repository.CtgCfgCoaVersionRepository;
import ngvgroup.com.fac.feature.regulated_account.service.CtgCfgCoaVersionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CtgCfgCoaVersionServiceImpl implements CtgCfgCoaVersionService {
    private final CtgCfgCoaVersionRepository repo;

    @Override
    public List<CtgCfgCoaVersionDTO> getAllData() {
        return this.repo.getAll();
    }

    @Override
    public List<CtgCfgCoaVersionDTO> getAllWithCommon(String commonTypeCode){
        return repo.getAllWithCommon(commonTypeCode);
    }
}
