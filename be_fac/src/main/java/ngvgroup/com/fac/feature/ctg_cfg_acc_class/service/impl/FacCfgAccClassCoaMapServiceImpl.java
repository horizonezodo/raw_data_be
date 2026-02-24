package ngvgroup.com.fac.feature.ctg_cfg_acc_class.service.impl;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.dto.FacCfgAccClassCoaMapDto;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.dto.FacCfgAccClassCoaMapResDto;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.mapper.FacCfgAccClassCoaMapMapper;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.model.FacCfgAccClassCoaMap;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.repository.FacCfgAccClassCoaMapRepository;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.service.FacCfgAccClassCoaMapService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FacCfgAccClassCoaMapServiceImpl
        extends BaseServiceImpl<FacCfgAccClassCoaMap, FacCfgAccClassCoaMapDto>
        implements FacCfgAccClassCoaMapService {

    private final FacCfgAccClassCoaMapRepository facCfgAccClassCoaMapRepository;
    private final FacCfgAccClassCoaMapMapper facCfgAccClassCoaMapMapper;

    public FacCfgAccClassCoaMapServiceImpl(
            FacCfgAccClassCoaMapRepository facCfgAccClassCoaMapRepository,

            FacCfgAccClassCoaMapMapper facCfgAccClassCoaMapMapper
    ) {
        super(facCfgAccClassCoaMapRepository, facCfgAccClassCoaMapMapper);
        this.facCfgAccClassCoaMapRepository = facCfgAccClassCoaMapRepository;
        this.facCfgAccClassCoaMapMapper=facCfgAccClassCoaMapMapper;
    }

    @Override
    public void create(List<FacCfgAccClassCoaMapDto> facCfgAccClassCoaMapDtos){
        for (FacCfgAccClassCoaMapDto f: facCfgAccClassCoaMapDtos) {
            f.setOrgCode(FacVariableConstants.ORG_CODE_ALL);
            create(f);
        }
    }

    @Override
    @Transactional
    public void update(List<FacCfgAccClassCoaMapDto> facCfgAccClassCoaMapDtos,String accClassCode) {
        List<FacCfgAccClassCoaMap> oldList =
                facCfgAccClassCoaMapRepository.getAllByAccClassCode(accClassCode);

        Set<Long> requestIds = facCfgAccClassCoaMapDtos.stream()
                .map(FacCfgAccClassCoaMapDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        for (FacCfgAccClassCoaMap old : oldList) {
            if (!requestIds.contains(old.getId())) {
                delete(old.getId());
            }
        }

        for (FacCfgAccClassCoaMapDto f : facCfgAccClassCoaMapDtos) {
            if (f.getId() != null) {
                update(f.getId(), f);
            } else {
                create(f);
            }
        }
    }

    @Override
    public Page<FacCfgAccClassCoaMapDto> searchAll(String keyword,String accClassCode,Pageable pageable){
        return facCfgAccClassCoaMapRepository.searchAll(keyword,accClassCode,pageable);
    }

    @Override
    public FacCfgAccClassCoaMapDto getByOrgCodeAndAccClassCode(String orgCode,String accClassCode){
        return facCfgAccClassCoaMapMapper.toDto(facCfgAccClassCoaMapRepository.getByOrgCodeAndAccClassCode(orgCode,accClassCode));
    }

    @Override
    public void delete(String accClassCode){
        List<FacCfgAccClassCoaMap> facCfgAccClassCoaMaps=facCfgAccClassCoaMapRepository.getAllByAccClassCode(accClassCode);
        facCfgAccClassCoaMapRepository.deleteAll(facCfgAccClassCoaMaps);
    }
}

