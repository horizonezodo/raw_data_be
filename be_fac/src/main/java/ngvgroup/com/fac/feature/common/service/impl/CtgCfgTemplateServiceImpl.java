package ngvgroup.com.fac.feature.common.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.fac.feature.common.dto.TemplateResDto;
import ngvgroup.com.fac.feature.common.repository.ComCfgTemplateRepository;
import ngvgroup.com.fac.feature.common.service.CtgCfgTemplateService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CtgCfgTemplateServiceImpl implements CtgCfgTemplateService {
    private final ComCfgTemplateRepository comCfgTemplateRepository;

    @Override
    public TemplateResDto getTemplateByCode(String templateCode) {
        TemplateResDto dto = comCfgTemplateRepository.findByTemplateCode(templateCode).orElseThrow(()
                -> new BusinessException(ErrorCode.NOT_FOUND, "TemplateCode " + templateCode));
        dto.setFilePath(String.format("template/%s/%s", dto.getTemplateCode(), dto.getFilePath()));
        dto.setMappingFilePath(String.format("template/%s/%s", dto.getTemplateCode(), dto.getMappingFilePath()));
        return dto;
    }
}
