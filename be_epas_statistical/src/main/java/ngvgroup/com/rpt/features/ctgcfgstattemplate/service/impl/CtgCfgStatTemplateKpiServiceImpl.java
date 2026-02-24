package ngvgroup.com.rpt.features.ctgcfgstattemplate.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.core.constant.VariableConstants;
import ngvgroup.com.rpt.features.ctgcfgstatkpi.model.CtgCfgStatKpi;
import ngvgroup.com.rpt.features.ctgcfgstatkpi.repository.CtgCfgStatKpiRepository;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatekpi.CtgCfgStatTemplateKpiDTO;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatekpi.IndexKpiDto;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatekpi.IndexKpiRequestDto;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.mapper.CtgCfgStatTemplateKpiMapper;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatTemplateKpi;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.repository.CtgCfgStatTemplateKpiRepository;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.service.CtgCfgStatTemplateKpiService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CtgCfgStatTemplateKpiServiceImpl implements CtgCfgStatTemplateKpiService {
    private final CtgCfgStatTemplateKpiRepository ctgCfgStatTemplateKpiRepository;
    private final CtgCfgStatTemplateKpiMapper ctgCfgStatTemplateKpiMapper;
    private final CtgCfgStatKpiRepository ctgCfgStatKpiRepository;

    @Override
    public void createTemplateKpi(List<CtgCfgStatTemplateKpiDTO> dtos, String templateCode) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }
        String orgCode = VariableConstants.ORG;
        List<String> templateKpiCodes = dtos.stream()
                .map(CtgCfgStatTemplateKpiDTO::getTemplateKpiCode)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        List<String> existedCodes = ctgCfgStatTemplateKpiRepository
                .findExistingTemplateKpiCodesAndTemplateCode(templateKpiCodes, orgCode, templateCode);

        if (!existedCodes.isEmpty()) {
            String errorMessage = "Các Template KPI Code đã tồn tại: " + String.join(", ", existedCodes);
            throw new BusinessException(StatisticalErrorCode.TEMPLATE_IS_NOT_EXISTS, errorMessage);
        }
        List<CtgCfgStatTemplateKpi> entities = dtos.stream()
                .map(ctgCfgStatTemplateKpiMapper::toEntity).toList();
        entities.forEach(e -> {
            e.setTemplateCode(templateCode);
            e.setOrgCode(orgCode);
            e.setRecordStatus(VariableConstants.DD);
        });

        ctgCfgStatTemplateKpiRepository.saveAll(entities);
    }

    @Override
    @Transactional
    public void updateTemplateKpi(List<CtgCfgStatTemplateKpiDTO> dtos, String templateCode) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }
        String orgCode = VariableConstants.ORG;
        List<CtgCfgStatTemplateKpi> dbEntities =
                ctgCfgStatTemplateKpiRepository.findAllByTemplateCodeAndOrgCode(templateCode, orgCode);

        Map<String, CtgCfgStatTemplateKpi> dbMap = dbEntities.stream()
                .collect(Collectors.toMap(CtgCfgStatTemplateKpi::getTemplateKpiCode, e -> e));

        List<String> feCodes = dtos.stream()
                .map(CtgCfgStatTemplateKpiDTO::getTemplateKpiCode)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<CtgCfgStatTemplateKpi> toSave = new ArrayList<>();
        for (CtgCfgStatTemplateKpiDTO dto : dtos) {
            CtgCfgStatTemplateKpi entity = dbMap.get(dto.getTemplateKpiCode());
            if (entity != null) {
                ctgCfgStatTemplateKpiMapper.updateEntityFromDto(dto, entity);
            } else {
                entity = ctgCfgStatTemplateKpiMapper.toEntity(dto);
                entity.setTemplateCode(templateCode);
                entity.setOrgCode(orgCode);
                entity.setRecordStatus(VariableConstants.DD);
            }
            entity.setRecordStatus(VariableConstants.DD);
            toSave.add(entity);
        }

        List<String> toDeleteCodes = dbEntities.stream()
                .map(CtgCfgStatTemplateKpi::getTemplateKpiCode)
                .filter(code -> !feCodes.contains(code))
                .toList();

        if (!toSave.isEmpty()) {
            ctgCfgStatTemplateKpiRepository.saveAll(toSave);
        }
        if (!toDeleteCodes.isEmpty()) {
            ctgCfgStatTemplateKpiRepository.deleteAllByTemplateCodeAndTemplateKpiCodeInAndOrgCode(
                    templateCode, toDeleteCodes, orgCode);
        }
    }

    @Override
    public List<CtgCfgStatTemplateKpiDTO> getAllByTemplateCode(String templateCode) {
        List<CtgCfgStatTemplateKpiDTO> dtos =
                ctgCfgStatTemplateKpiRepository.findAllByTemplateCode(templateCode)
                        .stream()
                        .map(ctgCfgStatTemplateKpiMapper::toDto)
                        .toList();

        dtos.forEach(dto -> {
            String kpiName = Optional.ofNullable(dto.getKpiCode())
                    .map(code -> ctgCfgStatKpiRepository.getByKpiCode(code))
                    .map(CtgCfgStatKpi::getKpiName)
                    .orElse(null);
            dto.setKpiName(kpiName);
        });

        return dtos;
    }

    @Override
    @Transactional
    @Modifying
    public void deleteTemplateKpi(String templateCode) {
        List<CtgCfgStatTemplateKpi> lst = this.ctgCfgStatTemplateKpiRepository.findAllByTemplateCode(templateCode);
        this.ctgCfgStatTemplateKpiRepository.deleteAll(lst);
    }

    @Override
    public List<IndexKpiDto> getKpiByIndex(List<IndexKpiRequestDto> request) {
        List<IndexKpiDto> allKpis = new ArrayList<>();
        for (IndexKpiRequestDto req : request) {
            List<IndexKpiDto> kpis = this.ctgCfgStatTemplateKpiRepository.getAllByTemplateCodes(
                    req.getIndexs(),
                    req.getTemplateCode()
            );
            allKpis.addAll(kpis);
        }
        return allKpis;
    }

    @Override
    public CtgCfgStatTemplateKpiDTO getByTemplateKpiCode(String templateKpiCode) {
        return ctgCfgStatTemplateKpiMapper.toDto(this.ctgCfgStatTemplateKpiRepository.findByTemplateKpiCode(templateKpiCode));
    }

}
