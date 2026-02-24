package ngvgroup.com.loan.feature.common.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import ngvgroup.com.loan.feature.common.dto.CommonDto;
import ngvgroup.com.loan.feature.common.dto.CtgComCommonDTO;
import ngvgroup.com.loan.feature.common.mapper.CtgComCommonMapper;
import ngvgroup.com.loan.feature.common.repository.ComCfgTemplateRepository;
import ngvgroup.com.loan.feature.common.repository.CtgComCommonRepository;
import ngvgroup.com.loan.feature.common.service.CtgComCommonService;
import ngvgroup.com.loan.feature.common.dto.TemplateResDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CtgComCommonServiceImpl implements CtgComCommonService {
    private final CtgComCommonMapper mapper;
    private final ComCfgTemplateRepository comCfgTemplateRepository;
    private final CtgComCommonRepository ctgComCommonRepository;

    @Override
    public List<CommonDto> listCommon(String commonTypeCode) {
        return ctgComCommonRepository.listCommon(commonTypeCode);
    }

    @Override
    public List<CommonDto> getAllCommon() {
        return ctgComCommonRepository.getAllCommon();
    }

    @Override
    public List<CtgComCommonDTO> findAllByCommonTypeCode(String commonTypeCode) {
        return ctgComCommonRepository.findAllByCommonTypeCode(commonTypeCode).stream()
                .map(mapper::toDto).toList();
    }

    @Override
    public TemplateResDto getTemplateByCode(String templateCode) {
        TemplateResDto dto = comCfgTemplateRepository.findByTemplateCode(templateCode).orElseThrow(()
                -> new BusinessException(ErrorCode.NOT_FOUND, "TemplateCode " + templateCode));
        dto.setFilePath(String.format("template/%s/%s", dto.getTemplateCode(), dto.getFilePath()));
        dto.setMappingFilePath(String.format("template/%s/%s", dto.getTemplateCode(), dto.getMappingFilePath()));
        return dto;
    }

}
