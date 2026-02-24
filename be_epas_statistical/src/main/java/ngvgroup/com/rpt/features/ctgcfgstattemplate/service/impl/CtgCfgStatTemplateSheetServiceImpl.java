package ngvgroup.com.rpt.features.ctgcfgstattemplate.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.core.constant.VariableConstants;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatesheet.CtgCfgStatTemplateSheetDTO;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.mapper.CtgCfgStatTemplateSheetMapper;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatTemplateSheet;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.repository.CtgCfgStatTemplateSheetRepository;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.service.CtgCfgStatTemplateSheetService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CtgCfgStatTemplateSheetServiceImpl implements CtgCfgStatTemplateSheetService {
    private final CtgCfgStatTemplateSheetRepository ctgCfgStatTemplateSheetRepository;
    private final CtgCfgStatTemplateSheetMapper ctgCfgStatTemplateSheetMapper;

    @Override
    public void createTemplateSheet(List<CtgCfgStatTemplateSheetDTO> dtos, String templateCode) {
        if (!this.validateData(dtos)) return;
        String orgCode = VariableConstants.ORG;

        List<String> templateCodes = dtos.stream()
                .map(CtgCfgStatTemplateSheetDTO::getTemplateCode)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<String> existedCodes = ctgCfgStatTemplateSheetRepository
                .findExistingTemplateCodesAndTemplateCode(templateCodes, orgCode, templateCode);

        if (!existedCodes.isEmpty()) {
            String errorMessage = "TemplateCode đã tồn tại trong hệ thống: " + String.join(", ", existedCodes);
            throw new BusinessException(StatisticalErrorCode.TEMPLATE_IS_NOT_EXISTS, errorMessage);
        }
        List<CtgCfgStatTemplateSheet> entities = dtos.stream()
                .map(ctgCfgStatTemplateSheetMapper::toEntity).toList();

        entities.forEach(e -> {
            e.setTemplateCode(templateCode);
            e.setOrgCode(orgCode);
            e.setRecordStatus(VariableConstants.DD);
        });

        ctgCfgStatTemplateSheetRepository.saveAll(entities);
    }


    @Override
    @Transactional
    public void updateTemplateSheet(List<CtgCfgStatTemplateSheetDTO> dtos, String templateCode) {
        if (!this.validateData(dtos)) return;
        String orgCode = VariableConstants.ORG;

        List<CtgCfgStatTemplateSheet> dbSheets =
                ctgCfgStatTemplateSheetRepository.findAllByTemplateCodeAndOrgCode(templateCode, orgCode);

        Map<Long, CtgCfgStatTemplateSheet> dbMap = dbSheets.stream()
                .collect(Collectors.toMap(CtgCfgStatTemplateSheet::getId, e -> e));

        List<CtgCfgStatTemplateSheet> toSave = new ArrayList<>();

        for (CtgCfgStatTemplateSheetDTO dto : dtos) {
            CtgCfgStatTemplateSheet entity;
            if (dto.getId() != null && dbMap.containsKey(dto.getId())) {
                entity = dbMap.get(dto.getId());
                this.ctgCfgStatTemplateSheetMapper.updateEntityFromDto(dto, entity);
            } else {
                entity = this.ctgCfgStatTemplateSheetMapper.toEntity(dto);
                entity.setTemplateCode(templateCode);
                entity.setOrgCode(orgCode);
                entity.setRecordStatus(VariableConstants.DD);
            }
            entity.setRecordStatus(VariableConstants.DD);
            toSave.add(entity);
        }

        Set<Long> feIds = dtos.stream()
                .map(CtgCfgStatTemplateSheetDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<CtgCfgStatTemplateSheet> toDelete = dbSheets.stream()
                .filter(sheet -> !feIds.contains(sheet.getId()))
                .toList();

        if (!toSave.isEmpty()) {
            ctgCfgStatTemplateSheetRepository.saveAll(toSave);
        }

        if (!toDelete.isEmpty()) {
            ctgCfgStatTemplateSheetRepository.deleteAll(toDelete);
        }
    }


    @Override
    @Transactional
    @Modifying
    public void deleteTemplateSheet(String templateCode) {
        List<CtgCfgStatTemplateSheet> lst = this.ctgCfgStatTemplateSheetRepository.findAllByTemplateCode(templateCode);
        this.ctgCfgStatTemplateSheetRepository.deleteAll(lst);
    }

    @Override
    public List<CtgCfgStatTemplateSheetDTO> getAllTemplateSheetByTemplateCode(String templateCode) {
        return this.ctgCfgStatTemplateSheetMapper.toListDto(this.ctgCfgStatTemplateSheetRepository.findAllByTemplateCode(templateCode));
    }

    public boolean validateData(List<CtgCfgStatTemplateSheetDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return false;
        }
        for (CtgCfgStatTemplateSheetDTO dto : dtos) {
            if ((dto.getTableData() == null || dto.getTableData().isBlank()) && (dto.getColumnEnd() == 0) && dto.getColumnStart() == 0 && dto.getRowStart() == 0 && dto.getRowEnd() == 0 && dto.getAreaId() == 0 && dto.getRowToDelete() == 0)
                return false;
        }
        return true;
    }

}
