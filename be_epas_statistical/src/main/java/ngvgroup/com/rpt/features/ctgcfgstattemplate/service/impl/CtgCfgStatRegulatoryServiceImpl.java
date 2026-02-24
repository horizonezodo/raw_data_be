package ngvgroup.com.rpt.features.ctgcfgstattemplate.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.CtgCfgStatRegulatoryDto;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.StatRegulatoryExcelDto;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.StatRegulatoryInfo;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.CtgCfgStatRegulatoryRequest;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.CtgCfgStatRegulatorySearch;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.CtgCfgStatRegulatoryResponse;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.mapper.CtgCfgStatRegulatoryMapper;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatRegulatory;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.repository.CtgCfgStatRegulatoryRepository;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.service.CtgCfgStatRegulatoryService;
import ngvgroup.com.rpt.features.excel.service.ExcelService;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.specification.CtgCfgStatRegulatorySpecification;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ngvgroup.bpm.core.common.util.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class CtgCfgStatRegulatoryServiceImpl implements CtgCfgStatRegulatoryService {
    private final CtgCfgStatRegulatoryRepository ctgCfgStatRegulatoryRepo;
    private final CtgCfgStatRegulatoryMapper mapper;
    private final ExcelService excelService;


    @Override
    public Page<CtgCfgStatRegulatoryDto> getAllCtgCfgStatRegulatory(CtgCfgStatRegulatorySearch request, Pageable pageable) {
        return ctgCfgStatRegulatoryRepo.findAll(CtgCfgStatRegulatorySpecification.build(request), pageable)
                .map(mapper::toDto);
    }

    @Override
    @Transactional
    public CtgCfgStatRegulatory addCtgCfgStatRegulatory(CtgCfgStatRegulatoryRequest request) throws BusinessException {
        boolean existCode = ctgCfgStatRegulatoryRepo.existsByStatRegulatoryCode(request.getStatRegulatoryCode());
        if (existCode) {
            throw new BusinessException(StatisticalErrorCode.VALID_STAT_REGULATORY_CODE);
        }
        if (isBlank(request.getStatRegulatoryCode()) || isBlank(request.getStatRegulatoryName())) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }

        if (isBlank(request.getCommonCode()) || isBlank(request.getCommonCode())) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }

        if (isBlank(request.getStatTypeCode())) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }

        if(request.getSortNumber()==null){
            request.setSortNumber(0);
        }

        CtgCfgStatRegulatory entity = getCtgCfgStatRegulatory(request);
        return ctgCfgStatRegulatoryRepo.save(entity);
    }

    @Override
    @Transactional
    public CtgCfgStatRegulatory updateCtgCfgStatRegulatory(CtgCfgStatRegulatoryRequest request) {

        if (isBlank(request.getStatRegulatoryCode())) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }

        if (isBlank(request.getCommonCode()) || isBlank(request.getCommonCode())) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }

        CtgCfgStatRegulatory existing = ctgCfgStatRegulatoryRepo.findById(request.getId()).orElseThrow(
                () -> new BusinessException(StatisticalErrorCode.NOT_EMPTY)
        );
        mapToExisting(existing, request);
        return ctgCfgStatRegulatoryRepo.save(existing);
    }

    @Override
    public CtgCfgStatRegulatoryResponse getDetail(String statRegulatoryCode) {
        return ctgCfgStatRegulatoryRepo.findDetail(statRegulatoryCode);
    }

    @Override
    @Transactional
    public void deleteCtgCfgStatRegulatory(Long id) throws BusinessException {
        CtgCfgStatRegulatory existing = ctgCfgStatRegulatoryRepo.findById(id).orElseThrow(
                () -> new BusinessException(StatisticalErrorCode.NOT_EMPTY)
        );
        ctgCfgStatRegulatoryRepo.delete(existing);
    }

    @Override
    public List<StatRegulatoryInfo> getStatRegulatoryByStatTypeCode(String statTypeCode, String keyword) {
        return ctgCfgStatRegulatoryRepo.getStatRegulatoryByStatTypeCode(statTypeCode, keyword);
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels, List<String> statRegulatoryCode, String fileName) {
        List<CtgCfgStatRegulatory> data;
        if (statRegulatoryCode != null && !statRegulatoryCode.isEmpty()) {
            data = ctgCfgStatRegulatoryRepo.findAll().stream()
                    .filter(statRegulatory -> statRegulatoryCode.contains(statRegulatory.getStatRegulatoryCode()))
                    .toList();
        } else {
            data = ctgCfgStatRegulatoryRepo.findAll();
        }
        List<StatRegulatoryExcelDto> excelData =mapper.entiToDto(data);
        return excelService.exportToExcel(excelData, labels, StatRegulatoryExcelDto.class, fileName);
    }

    private static CtgCfgStatRegulatory getCtgCfgStatRegulatory(CtgCfgStatRegulatoryRequest request) {

        CtgCfgStatRegulatory entity = new CtgCfgStatRegulatory();
        entity.setStatRegulatoryCode(request.getStatRegulatoryCode());
        entity.setStatRegulatoryName(request.getStatRegulatoryName());
        entity.setReportModuleCode(request.getCommonCode());
        entity.setStatTypeCode(request.getStatTypeCode());
        entity.setSortNumber(Long.valueOf(request.getSortNumber()));
        entity.setDescription(request.getDescription());
        entity.setRecordStatus("approval");
        entity.setOrgCode("%");
        return entity;
    }

    private static void mapToExisting(CtgCfgStatRegulatory existing, CtgCfgStatRegulatoryRequest request) {
        if (isNotBlank(request.getStatRegulatoryName())) {
            existing.setStatRegulatoryName(request.getStatRegulatoryName());
        }
        if (isNotBlank(request.getCommonCode())) {
            existing.setReportModuleCode(request.getCommonCode());
        }
        if (isNotBlank(request.getStatTypeCode())) {
            existing.setStatTypeCode(request.getStatTypeCode());
        }
        if (request.getIsActive() != null) {
            existing.setIsActive(request.getIsActive());
        }
        if (request.getSortNumber() != null) {
            existing.setSortNumber(Long.valueOf(request.getSortNumber()));
        }
        if (isNotBlank(request.getDescription())) {
            existing.setDescription(request.getDescription());
        }
    }


    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
