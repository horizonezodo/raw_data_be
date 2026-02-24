package ngvgroup.com.rpt.features.ctgcfgstatscorerank.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.AllArgsConstructor;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.CtgCfgStatScoreGroupDto;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreGroupRequest;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreGroupSearch;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.CtgCfgStatScoreGroupResponse;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreGroupExcel;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.mapper.CtgCfgStatScoreGroupMapper;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.model.CtgCfgStatScoreGroup;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.repository.CtgCfgStatScoreGroupRepository;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.service.CtgCfgStatScoreGroupService;
import ngvgroup.com.rpt.features.excel.service.ExcelService;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.specification.CtgCfgStatScoreGroupSpecification;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CtgCfgStatScoreGroupServiceImpl implements CtgCfgStatScoreGroupService {
    private final CtgCfgStatScoreGroupRepository repository;
    private final CtgCfgStatScoreGroupMapper mapper;
    private final ExcelService excelService;

    @Override
    public Page<CtgCfgStatScoreGroupDto> searchAll(String keyword, String statScoreTypeCode, Pageable pageable) {
        return repository.searchAll(keyword, statScoreTypeCode, pageable);
    }

    @Override
    public Page<CtgCfgStatScoreGroupResponse> getAll(StatScoreGroupSearch search, Pageable pageable) {
        return repository.findAll(CtgCfgStatScoreGroupSpecification.build(search), pageable)
                .map(mapper::toDto);
    }

    @Override
    @Transactional
    public CtgCfgStatScoreGroup add(StatScoreGroupRequest req) {

        if (repository.existsByStatScoreGroupCode(req.getStatScoreGroupCode())) {
            throw new BusinessException(StatisticalErrorCode.DUPLICATE_SCORE_GROUP_CODE);
        }

        if (req.getStatScoreGroupCode() == null) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }
        if (req.getStatScoreGroupName() == null) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }

        if (req.getStatScoreTypeCode() == null) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }

        if (req.getStatScoreGroupTypeCode() == null) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }
        CtgCfgStatScoreGroup statScoreGroup = mapper.toEntity(req);
        statScoreGroup.setOrgCode("%");
        statScoreGroup.setRecordStatus("approval");

        return repository.save(statScoreGroup);
    }

    @Override
    public CtgCfgStatScoreGroupResponse getDetail(String statScoreGroupCode) {
        Optional<CtgCfgStatScoreGroup> statScoreGroup = repository.getByStatScoreGroupCode(statScoreGroupCode);
        if (statScoreGroup.isEmpty()) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }
        return statScoreGroup.map(mapper::toDto).orElse(null);
    }

    @Override
    @Transactional
    public CtgCfgStatScoreGroup edit(StatScoreGroupRequest req) {
        CtgCfgStatScoreGroup optionalGroup = repository.getCtgCfgStatScoreGroupByStatScoreGroupCode(req.getStatScoreGroupCode());
        if (optionalGroup == null) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }
        mapper.updateEntityFromRequest(req, optionalGroup);
        return repository.save(optionalGroup);
    }

    @Override
    public void delete(String statScoreGroupCode) {
        CtgCfgStatScoreGroup optionalGroup = repository.getCtgCfgStatScoreGroupByStatScoreGroupCode(statScoreGroupCode);
        if (optionalGroup == null) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }
        repository.delete(optionalGroup);

    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels, List<String> statScoreGroupCode, String fileName) {
        List<CtgCfgStatScoreGroup> data;
        List<StatScoreGroupExcel>  excel;
        if (statScoreGroupCode != null && !statScoreGroupCode.isEmpty()) {
            data = repository.findAll().stream().filter(g -> false).toList();
            List<StatScoreGroupExcel> excelData = mapper.toExcelDto(data);

            return excelService.exportToExcel(excelData, labels, StatScoreGroupExcel.class, fileName);
        }else {
            excel = repository.findAllWithType();
            return excelService.exportToExcel(excel, labels, StatScoreGroupExcel.class, fileName);
        }
    }

    @Override
    public Boolean checkExistStatScoreGroupCode(String statScoreGroupCode) {
        return repository.existsByStatScoreGroupCode(statScoreGroupCode);
    }
}
