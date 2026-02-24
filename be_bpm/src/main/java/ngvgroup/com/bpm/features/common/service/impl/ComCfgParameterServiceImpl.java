package ngvgroup.com.bpm.features.common.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpm.core.base.dto.ComCfgParameterDto;
import ngvgroup.com.bpm.features.common.mapper.ComCfgParameterMapper;
import ngvgroup.com.bpm.features.common.repository.ComCfgParameterRepository;
import ngvgroup.com.bpm.features.common.service.ComCfgParameterService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComCfgParameterServiceImpl implements ComCfgParameterService {

    private final ComCfgParameterRepository comCfgParameterRepository;
    private final ComCfgParameterMapper comCfgParameterMapper;

    @Override
    public ComCfgParameterDto getParameterByCode(String paramCode) {
        return comCfgParameterRepository.findByParamCodeAndIsActiveTrueAndIsDeleteFalse(paramCode)
                .map(comCfgParameterMapper::toDto)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, paramCode));
    }
}
