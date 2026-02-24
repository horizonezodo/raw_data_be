package ngvgroup.com.rpt.features.ctgcfgworkflow.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import ngvgroup.com.rpt.core.constant.VariableConstants;
import ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow.CtgCfgStatScoreTypeWfDto;
import ngvgroup.com.rpt.features.ctgcfgworkflow.mapper.CtgCfgStatScoreTypeWfMapper;
import ngvgroup.com.rpt.features.ctgcfgworkflow.model.CtgCfgStatScoreTypeWf;
import ngvgroup.com.rpt.features.ctgcfgworkflow.repository.CtgCfgStatScoreTypeWfRepository;
import ngvgroup.com.rpt.features.ctgcfgworkflow.service.CtgCfgStatScoreTypeWfService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CtgCfgStatScoreTypeWfServiceImpl implements CtgCfgStatScoreTypeWfService {

    private final CtgCfgStatScoreTypeWfRepository ctgCfgStatScoreTypeWfRepository;

    @Override
    public void create(CtgCfgStatScoreTypeWfDto ctgCfgStatScoreTypeWfDto) {
        Optional<CtgCfgStatScoreTypeWf> ctgCfgStatScoreTypeWfOptional = ctgCfgStatScoreTypeWfRepository.findByStatScoreTypeCode(ctgCfgStatScoreTypeWfDto.getStatScoreTypeCode());
        if (ctgCfgStatScoreTypeWfOptional.isPresent()) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }

        ctgCfgStatScoreTypeWfDto.setOrgCode(VariableConstants.ORG);

        CtgCfgStatScoreTypeWf cfgStatScoreTypeWf = CtgCfgStatScoreTypeWfMapper.INSTANCE.toEntity(ctgCfgStatScoreTypeWfDto);
        cfgStatScoreTypeWf.setRecordStatus(VariableConstants.DD);
        ctgCfgStatScoreTypeWfRepository.save(cfgStatScoreTypeWf);
    }

    @Override
    @Transactional
    public void update(CtgCfgStatScoreTypeWfDto ctgCfgStatScoreTypeWfDto) {
        Optional<CtgCfgStatScoreTypeWf> ctgCfgStatScoreTypeWfOptional = ctgCfgStatScoreTypeWfRepository.findByStatScoreTypeCode(ctgCfgStatScoreTypeWfDto.getStatScoreTypeCode());
        if (ctgCfgStatScoreTypeWfOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        Long id = ctgCfgStatScoreTypeWfOptional.get().getId();
        ctgCfgStatScoreTypeWfDto.setOrgCode(VariableConstants.ORG);

        CtgCfgStatScoreTypeWfMapper.INSTANCE.updateEntityFromDto(ctgCfgStatScoreTypeWfDto, ctgCfgStatScoreTypeWfOptional.get());

        ctgCfgStatScoreTypeWfOptional.get().setRecordStatus(VariableConstants.DD);
        ctgCfgStatScoreTypeWfOptional.get().setId(id);
        ctgCfgStatScoreTypeWfRepository.save(ctgCfgStatScoreTypeWfOptional.get());
    }

    @Override
    public CtgCfgStatScoreTypeWfDto getDetail(String statScoreTypeCode) {
        Optional<CtgCfgStatScoreTypeWf> ctgCfgStatScoreTypeWfOptional = ctgCfgStatScoreTypeWfRepository.findByStatScoreTypeCode(statScoreTypeCode);
        if (ctgCfgStatScoreTypeWfOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        return CtgCfgStatScoreTypeWfMapper.INSTANCE.toDto(ctgCfgStatScoreTypeWfOptional.get());
    }

    @Override
    public void delete(String statScoreTypeCode) {
        Optional<CtgCfgStatScoreTypeWf> ctgCfgStatScoreTypeWfOptional = ctgCfgStatScoreTypeWfRepository.findByStatScoreTypeCode(statScoreTypeCode);
        if (ctgCfgStatScoreTypeWfOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        ctgCfgStatScoreTypeWfRepository.delete(ctgCfgStatScoreTypeWfOptional.get());
    }

}
