package ngvgroup.com.rpt.features.ctgcfgstattemplate.service.impl;

import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.AllArgsConstructor;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.features.common.repository.ComCfgCommonRepository;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.CtgCfgStatTypeDto;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.CtgCfgStatTypeListDto;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.CtgCfgStatTypeResponse;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.StatFilterTreeDto;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.StatFilterTreeItem;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.mapper.CtgCfgStatTypeMapper;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatType;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.repository.CtgCfgStatTypeRepository;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.service.CtgCfgStatTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CtgCfgStatTypeServiceImpl implements CtgCfgStatTypeService {
    private final CtgCfgStatTypeRepository repository;
    private final ComCfgCommonRepository commonRepository;
    private final ExportExcel exportExcel;

    private final CtgCfgStatTypeRepository ctgCfgStatTypeRepo;

    @Override
    public List<CtgCfgStatTypeResponse> getStatTypeByReportModuleCode(String commonCode) {
        return ctgCfgStatTypeRepo.getStatTypeByReportModuleCode(commonCode);
    }

    @Override
    public List<CtgCfgStatTypeResponse> getAllStatType(String keyword) {
        return ctgCfgStatTypeRepo.getAllStatType(keyword);
    }

    @Override
    public List<StatFilterTreeDto> getStatFilterTree() {
        List<StatFilterTreeItem> items = ctgCfgStatTypeRepo.getFilterTree();
        Map<String, StatFilterTreeDto> map = new LinkedHashMap<>();
        for (StatFilterTreeItem item : items) {
            map.computeIfAbsent(item.getReportModuleCode(), code -> new StatFilterTreeDto(
                    item.getReportModuleCode(),
                    item.getCommonCode(),
                    item.getCommonName(),
                    new ArrayList<>())).getTypes().add(new CtgCfgStatTypeDto(
                    item.getStatTypeCode(),
                    item.getStatTypeName()));
        }

        return new ArrayList<>(map.values());
    }

    @Override
    public Page<CtgCfgStatTypeListDto> search(String keyword, List<String> reportModuleCodes,
                                              Pageable pageable) {
        if (reportModuleCodes == null || reportModuleCodes.isEmpty()) {
            return Page.empty(pageable);
        }

        Page<CtgCfgStatType> page = repository.search(keyword, reportModuleCodes, pageable);
        return page.map(entity -> {
            CtgCfgStatTypeListDto dto = CtgCfgStatTypeMapper.INSTANCE.toDto(entity);
            commonRepository
                    .findByCommonTypeCodeAndIsActiveTrue("CM002")
                    .stream()
                    .filter(c -> c.getCommonCode().equals(entity.getReportModuleCode()))
                    .findFirst().ifPresent(common -> dto.setReportModuleName(common.getCommonName()));
            return dto;
        });
    }

    @Override
    public CtgCfgStatType detail(String statTypeCode) {
        return repository.findByStatTypeCode(statTypeCode);
    }

    @Override
    public CtgCfgStatType create(CtgCfgStatType payload) {
        boolean existsByStatTypeCode = repository.existsByStatTypeCode(payload.getStatTypeCode());
        if (existsByStatTypeCode) {
            throw new BusinessException(StatisticalErrorCode.VALID_STAT_TYPE_CODE);
        }
        if (payload.getOrgCode() == null || payload.getOrgCode().isBlank()) {
            payload.setOrgCode("%");
        }
        if (payload.getRecordStatus() == null || payload.getRecordStatus().isBlank()) {
            payload.setRecordStatus("approval");
        }

        if (payload.getIsActive() == null) {
            payload.setIsActive(1);
        }

        return repository.save(payload);
    }

    @Override
    public CtgCfgStatType update(String statTypeCode, CtgCfgStatType payload) {
        CtgCfgStatType current = repository.findByStatTypeCode(statTypeCode);
        current.setStatTypeName(payload.getStatTypeName());
        current.setReportModuleCode(payload.getReportModuleCode());
        if (payload.getIsActive() != null) {
            current.setIsActive(payload.getIsActive());
        }
        if (payload.getOrgCode() != null && !payload.getOrgCode().isBlank()) {
            current.setOrgCode(payload.getOrgCode());
        } else if (current.getOrgCode() == null || current.getOrgCode().isBlank()) {
            current.setOrgCode("%");
        }
        current.setExpressionSql(payload.getExpressionSql());
        current.setSortNumber(payload.getSortNumber());
        return repository.save(current);
    }

    @Override
    public void delete(String statTypeCode) {
        CtgCfgStatType current = repository.findByStatTypeCode(statTypeCode);
        repository.delete(current);
    }

    @Override
    public ResponseEntity<byte[]> exportToExcel(List<String> reportModuleCodes, String fileName, String keyword, Pageable pageable) {
        try {
            Page<CtgCfgStatTypeListDto> page =
                    this.search(keyword, reportModuleCodes, pageable);
            List<CtgCfgStatTypeListDto> data = page.getContent();
            return exportExcel.exportExcel(data, fileName);
        } catch (Exception e) {
            throw new BusinessException(StatisticalErrorCode.STAT_TYPE_EXPORT_ERR);
        }

    }

    @Override
    public boolean existsByCode(String statTypeCode) {
        return repository.existsByStatTypeCode(statTypeCode);
    }
}
