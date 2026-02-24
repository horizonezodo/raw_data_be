package ngvgroup.com.rpt.features.ctgcfgstatruledefine.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.features.ctgcfgstatruledefine.dto.ReqSearchStatRuleDefineDto;
import ngvgroup.com.rpt.features.ctgcfgstatruledefine.dto.ResponseSearchStatRuleDefineDto;
import ngvgroup.com.rpt.features.ctgcfgstatruledefine.dto.StatRuleDefineDto;
import ngvgroup.com.rpt.features.ctgcfgstatruledefine.mapper.StatRuleDefineMapper;
import ngvgroup.com.rpt.features.ctgcfgstatruledefine.model.StatRuleDefine;
import ngvgroup.com.rpt.features.ctgcfgstatruledefine.repository.StatRuleDefineRepository;
import ngvgroup.com.rpt.features.ctgcfgstatruledefine.service.StatRuleDefineService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StatRuleDefineServiceImpl
        extends BaseServiceImpl<StatRuleDefine, StatRuleDefineDto>
        implements StatRuleDefineService {

    private final StatRuleDefineRepository statRuleDefineRepository;

    public StatRuleDefineServiceImpl(
            StatRuleDefineRepository repository,
            StatRuleDefineMapper mapper
    ) {
        super(repository, mapper);
        this.statRuleDefineRepository = repository;
    }

    @Override
    protected void validateBeforeCreate(StatRuleDefineDto dto) {
        statRuleDefineRepository.findByRuleCode(dto.getRuleCode())
                .ifPresent(existing -> {
                    String message = dto.getRuleCode() + " đã tồn tại trong hệ thống!";
                    throw new BusinessException(StatisticalErrorCode.DUPLICATE_RULE_CODE, message);
                });
    }

    @Override
    protected void validateBeforeUpdate(StatRuleDefineDto dto, StatRuleDefine entity) {
        statRuleDefineRepository.findByRuleCode(dto.getRuleCode())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(entity.getId())) {
                        throw new BusinessException(StatisticalErrorCode.NOT_EXISTS, dto.getRuleCode());
                    }
                });
    }

    @Override
    protected void beforeSaveCreate(StatRuleDefine entity, StatRuleDefineDto dto) {
        entity.setRecordStatus("approval");
        entity.setOrgCode("%");
    }

    @Override
    public Page<ResponseSearchStatRuleDefineDto> search(String search, ReqSearchStatRuleDefineDto body, Pageable pageable) {
        return statRuleDefineRepository.search(search, body.getRuleTypeCodes(), pageable);
    }
}
