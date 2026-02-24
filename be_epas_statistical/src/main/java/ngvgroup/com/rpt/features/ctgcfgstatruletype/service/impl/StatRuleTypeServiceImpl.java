package ngvgroup.com.rpt.features.ctgcfgstatruletype.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.features.ctgcfgstatruletype.dto.ResponseSearchStatRuleTypeDto;
import ngvgroup.com.rpt.features.ctgcfgstatruletype.dto.StatRuleTypeDto;
import ngvgroup.com.rpt.features.ctgcfgstatruletype.mapper.StatRuleTypeMapper;
import ngvgroup.com.rpt.features.ctgcfgstatruletype.model.StatRuleType;
import ngvgroup.com.rpt.features.ctgcfgstatruletype.repository.StatRuleTypeRepository;
import ngvgroup.com.rpt.features.ctgcfgstatruletype.service.StatRuleTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StatRuleTypeServiceImpl extends BaseServiceImpl<StatRuleType, StatRuleTypeDto> implements StatRuleTypeService {

    private final StatRuleTypeRepository statRuleTypeRepository;

    public StatRuleTypeServiceImpl(
            StatRuleTypeRepository repository,
            StatRuleTypeMapper mapper
    ) {
        super(repository, mapper);
        this.statRuleTypeRepository = repository;
    }


    @Override
    public Page<ResponseSearchStatRuleTypeDto> search(String search, Pageable page) {
        return statRuleTypeRepository.search(search, page);
    }

    @Override
    protected void validateBeforeCreate(StatRuleTypeDto dto) {
        statRuleTypeRepository.findByRuleTypeCode(dto.getRuleTypeCode())
                .ifPresent(existing -> {
                    String message =  dto.getRuleTypeCode() + " đã tồn tại trong hệ thống!";
                    throw new BusinessException(StatisticalErrorCode.DUPLICATE_RULE_TYPE_CODE, message);
                });
    }

    @Override
    protected void validateBeforeUpdate(StatRuleTypeDto dto, StatRuleType entity) {
        statRuleTypeRepository.findByRuleTypeCode(dto.getRuleTypeCode())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(entity.getId())) {
                        throw new BusinessException(StatisticalErrorCode.NOT_EXISTS, dto.getRuleTypeCode());
                    }
                });
    }

    @Override
    protected void beforeSaveCreate(StatRuleType entity, StatRuleTypeDto dto) {
        entity.setRecordStatus("approval");
        entity.setOrgCode("%");
    }
}
