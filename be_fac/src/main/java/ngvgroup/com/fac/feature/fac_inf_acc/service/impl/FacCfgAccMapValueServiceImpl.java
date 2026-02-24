package ngvgroup.com.fac.feature.fac_inf_acc.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.fac.core.constant.FacErrorCode;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.AccMapValueDto;
import ngvgroup.com.fac.feature.fac_inf_acc.mapper.FacCfgAccMapValueMapper;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacCfgAccMapValue;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacCfgAccStructureDtl;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacCfgAccMapValueRepository;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacCfgAccStructureDtlRepository;
import ngvgroup.com.fac.feature.fac_inf_acc.service.FacCfgAccMapValueService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacCfgAccMapValueServiceImpl extends BaseServiceImpl<FacCfgAccMapValue, AccMapValueDto> implements FacCfgAccMapValueService {
    private final FacCfgAccMapValueRepository mapValueRepo;
    private final FacCfgAccStructureDtlRepository structureDtlRepo;

    protected FacCfgAccMapValueServiceImpl(
            FacCfgAccMapValueRepository mapValueRepo,
            FacCfgAccMapValueMapper mapValueMapper,
            FacCfgAccStructureDtlRepository structureDtlRepo) {
        super(mapValueRepo, mapValueMapper);
        this.mapValueRepo = mapValueRepo;
        this.structureDtlRepo = structureDtlRepo;
    }

    @Override
    public AccMapValueDto getDomainConfig() {
        FacCfgAccStructureDtl domainSegment = structureDtlRepo.findByAccStructureCodeAndSegmentCode(
                "ACC_INTERNAL",
                "DOMAIN").orElseThrow(() ->
                new BusinessException(FacErrorCode.DATA_NOT_FOUND)
        );

        String mapCode = domainSegment.getSegmentSource();

        List<AccMapValueDto.DomainItem> domains =
                mapValueRepo.findByMapCode(mapCode)
                        .stream()
                        .map(m -> {
                            AccMapValueDto.DomainItem d = new AccMapValueDto.DomainItem();
                            d.setBusinessCode(m.getBusinessCode());
                            return d;
                        })
                        .toList();

        AccMapValueDto res = new AccMapValueDto();
        res.setIsRequired(domainSegment.getIsRequired());
        res.setMapCode(mapCode);
        res.setDomains(domains);

        return res;
    }
}
