package ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.service.impl;

import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.dto.ResponseSearchStatResponseDefineDto;
import ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.dto.StatResponseDefineDto;
import ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.mapper.StatResponseDefineMapper;
import ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.model.StatResponseDefine;
import ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.repository.StatResponseDefineRepository;
import ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.service.StatResponseDefineService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class StatResponseDefineServiceImpl extends BaseServiceImpl<StatResponseDefine, StatResponseDefineDto> implements StatResponseDefineService {

    private final StatResponseDefineRepository statResponseDefineRepository;

    public StatResponseDefineServiceImpl(
            StatResponseDefineRepository repository,
            StatResponseDefineMapper mapper
    ) {
        super(repository, mapper);
        this.statResponseDefineRepository = repository;
    }

    @Override
    public Page<ResponseSearchStatResponseDefineDto> search(String search , Pageable page) {
        return statResponseDefineRepository.search(search , page);
    }

    @Override
    protected void validateBeforeCreate(StatResponseDefineDto dto) {
        statResponseDefineRepository.findByResponseCode(dto.getResponseCode())
                .ifPresent(existing -> {
                    throw new BusinessException(StatisticalErrorCode.NOT_EXISTS,dto.getResponseCode());
                });
    }

    @Override
    protected void validateBeforeUpdate(StatResponseDefineDto dto, StatResponseDefine entity) {
        statResponseDefineRepository.findByResponseCode(dto.getResponseCode())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(entity.getId())) {
                        throw new BusinessException(StatisticalErrorCode.NOT_EXISTS,dto.getResponseCode());
                    }
                });
    }

    @Override
    protected void beforeSaveCreate(StatResponseDefine entity, StatResponseDefineDto dto) {
        entity.setRecordStatus("approval");
        entity.setOrgCode("%");
    }

}
