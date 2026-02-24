package ngvgroup.com.rpt.features.ctgcfgstatus.service.impl;

import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.core.constant.VariableConstants;
import ngvgroup.com.rpt.features.ctgcfgstatus.dto.CtgCfgStatusSlaDto;
import ngvgroup.com.rpt.features.ctgcfgstatus.mapper.CtgCfgStatusSlaMapper;
import ngvgroup.com.rpt.features.ctgcfgstatus.model.CtgCfgStatusSla;
import ngvgroup.com.rpt.features.ctgcfgstatus.repository.CtgCfgStatusSlaRepository;
import ngvgroup.com.rpt.features.ctgcfgstatus.service.CtgCfgStatusSlaService;
import org.springframework.stereotype.Service;

@Service
public class CtgCfgStatusSlaServiceImpl extends BaseServiceImpl<CtgCfgStatusSla, CtgCfgStatusSlaDto> implements CtgCfgStatusSlaService {
    private final CtgCfgStatusSlaRepository repo;
    protected CtgCfgStatusSlaServiceImpl(
            CtgCfgStatusSlaRepository repository,
            CtgCfgStatusSlaMapper mapper
    ) {
        super(repository, mapper );
        this.repo = repository;
    }

    @Override
    public Long getIdByStatusCode(String statusCode) {
        return this.repo.findIdByStatusCode(statusCode);
    }

    @Override
    protected void validateBeforeCreate(CtgCfgStatusSlaDto dto) {
        this.repo.findByStatusCode(dto.getStatusCode())
                .ifPresent(existing -> {
                    String message = dto.getStatusCode() + " đã tồn tại trong hệ thống!";
                    throw new BusinessException(StatisticalErrorCode.DUPLICATE_RULE_CODE, message);
                });
    }

    @Override
    protected void validateBeforeUpdate(CtgCfgStatusSlaDto dto, CtgCfgStatusSla entity) {
        this.repo.findByStatusCode(dto.getStatusCode())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(entity.getId())) {
                        throw new BusinessException(StatisticalErrorCode.NOT_EXISTS, dto.getStatusCode());
                    }
                });
    }

    @Override
    protected void beforeSaveCreate(CtgCfgStatusSla entity, CtgCfgStatusSlaDto dto) {
        entity.setRecordStatus(VariableConstants.DD);
    }
}
