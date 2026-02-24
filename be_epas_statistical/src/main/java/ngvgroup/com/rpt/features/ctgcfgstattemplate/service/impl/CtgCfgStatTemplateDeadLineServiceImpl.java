package ngvgroup.com.rpt.features.ctgcfgstattemplate.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.core.constant.VariableConstants;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatedeadline.CtgCfgStatTemplateDeadLineDTO;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.mapper.CtgCfgStatTemplateDeadLineMapper;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatTemplateDeadLine;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.repository.CtgCfgStatTemplateDeadLineRepository;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.service.CtgCfgStatTemplateDeadLineService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CtgCfgStatTemplateDeadLineServiceImpl implements CtgCfgStatTemplateDeadLineService {
    private final CtgCfgStatTemplateDeadLineRepository repo;
    private final CtgCfgStatTemplateDeadLineMapper mapper;

    @Override
    public void createDeadLine(CtgCfgStatTemplateDeadLineDTO dto, String templateCode) {
        if(this.isInvalidData(dto)) return;
        String orgCode = VariableConstants.ORG;
        CtgCfgStatTemplateDeadLine deadLine = this.mapper.toEntity(dto);
        deadLine.setTemplateCode(templateCode);
        deadLine.setOrgCode(orgCode);
        deadLine.setRecordStatus(VariableConstants.DD);
        repo.save(deadLine);
    }

    @Override
    @Transactional
    public void updateDeadLine(CtgCfgStatTemplateDeadLineDTO dto, String templateCode) {
        if(this.isInvalidData(dto)) return;
        Optional<CtgCfgStatTemplateDeadLine> opt = this.repo.findByTemplateCode(templateCode);
        if(opt.isPresent()){
            CtgCfgStatTemplateDeadLine deadLine = opt.get();
            this.mapper.updateEntityFromDto(dto,deadLine);
            repo.save(deadLine);
        }else{
            this.createDeadLine(dto,templateCode);
        }
    }

    @Override
    public CtgCfgStatTemplateDeadLineDTO getAllByTemplateCode(String templateCode) {
        return repo.getAllByTemplateCode(templateCode);
    }

    @Override
    @Transactional
    @Modifying
    public void deleteAllByTemplateCode(String templateCode) {
        this.repo.deleteAllByTemplateCode(templateCode);
    }

    private boolean isInvalidData(CtgCfgStatTemplateDeadLineDTO dto) {
        return dto == null
                || dto.getFrequency() == null || dto.getFrequency().isBlank()
                || dto.getDeadlineType() == null || dto.getDeadlineType().isBlank();
    }

}
