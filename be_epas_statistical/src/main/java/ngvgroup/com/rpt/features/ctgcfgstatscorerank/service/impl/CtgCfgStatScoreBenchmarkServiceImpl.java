package ngvgroup.com.rpt.features.ctgcfgstatscorerank.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreBenchmarkDto;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreBenchmarkExcel;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreBenchmarkRequest;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.mapper.CtgCfgStatScoreBenchmarkMapper;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.model.CtgCfgStatScoreBenchmark;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.repository.CtgCfgStatScoreBenchmarkRepository;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.service.CtgCfgStatScoreBenchmarkService;
import ngvgroup.com.rpt.features.excel.service.ExcelService;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.specification.CtgCfgStatScoreBenchmarkSpecification;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CtgCfgStatScoreBenchmarkServiceImpl implements CtgCfgStatScoreBenchmarkService {
    private final CtgCfgStatScoreBenchmarkRepository repository;
    private final CtgCfgStatScoreBenchmarkMapper mapper;
    private final ExcelService excelService;

    @Override
    public Page<StatScoreBenchmarkDto> getAll(String keyword, Pageable pageable) {
        Specification<CtgCfgStatScoreBenchmark> spec = CtgCfgStatScoreBenchmarkSpecification.build(keyword);
        return repository.findAll(spec, pageable).map(mapper::toDto);
    }

    @Override
    public CtgCfgStatScoreBenchmark add(StatScoreBenchmarkRequest req) {

        if (repository.existsByBenchmarkCode(req.getBenchmarkCode())) {
            throw new BusinessException(StatisticalErrorCode.DUPLICATE_BENCHMARK_CODE);
        }

        if (req.getBenchmarkCode() == null) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }

        if (req.getBenchmarkName() == null) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }

        if (req.getStatScoreTypeCode() == null) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }

        if (req.getBenchmarkValue() == null) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }

        if (req.getScoreValueMax() == null) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }

        if (req.getScoreValueMin() == null) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }

        if (req.getScoreValueMin().compareTo(req.getScoreValueMax()) > 0) {
            throw new BusinessException(StatisticalErrorCode.MIN_GREATER_THAN_MAX);
        }

        List<CtgCfgStatScoreBenchmark> sameTypeRecords =
                repository.findByStatScoreTypeCode(req.getStatScoreTypeCode());
        BigDecimal newMin = req.getScoreValueMin();
        BigDecimal newMax = req.getScoreValueMax();

        for (CtgCfgStatScoreBenchmark existing : sameTypeRecords) {
            BigDecimal existingMin = existing.getScoreValueMin();
            BigDecimal existingMax = existing.getScoreValueMax();

            boolean overlap =
                    !(newMax.compareTo(existingMin) < 0 || newMin.compareTo(existingMax) > 0);

            if (overlap) {
                throw new BusinessException(StatisticalErrorCode.OVERLAP_SCORE_RANGE);
            }
        }

        CtgCfgStatScoreBenchmark scoreBenchmark = mapper.toEntity(req);
        scoreBenchmark.setOrgCode("%");
        scoreBenchmark.setRecordStatus("approval");
        return repository.save(scoreBenchmark);
    }

    @Override
    public StatScoreBenchmarkDto getDetail(String statScoreGroupCode) {
        CtgCfgStatScoreBenchmark benchmark = repository.getByBenchmarkCode(statScoreGroupCode);
        if (benchmark == null) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }
        return mapper.toDto(benchmark);
    }

    @Override
    public CtgCfgStatScoreBenchmark edit(StatScoreBenchmarkRequest req) {
        CtgCfgStatScoreBenchmark benchmark = repository.getByBenchmarkCode(req.getBenchmarkCode());
        if (benchmark == null) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }

        if (req.getBenchmarkName() == null ||
                req.getStatScoreTypeCode() == null ||
                req.getBenchmarkValue() == null ||
                req.getScoreValueMin() == null ||
                req.getScoreValueMax() == null) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }
        if (req.getScoreValueMin().compareTo(req.getScoreValueMax()) > 0) {
            throw new BusinessException(StatisticalErrorCode.MIN_GREATER_THAN_MAX);
        }
        List<CtgCfgStatScoreBenchmark> sameTypeRecords =
                repository.findByStatScoreTypeCode(req.getStatScoreTypeCode());
        BigDecimal newMin = req.getScoreValueMin();
        BigDecimal newMax = req.getScoreValueMax();

        for (CtgCfgStatScoreBenchmark existing : sameTypeRecords) {
            if (existing.getBenchmarkCode().equals(req.getBenchmarkCode())) {
                continue;
            }

            BigDecimal existingMin = existing.getScoreValueMin();
            BigDecimal existingMax = existing.getScoreValueMax();

            boolean overlap = !(newMax.compareTo(existingMin) < 0 || newMin.compareTo(existingMax) > 0);

            if (overlap) {
                throw new BusinessException(StatisticalErrorCode.OVERLAP_SCORE_RANGE);
            }
        }
        mapper.updateEntityFromRequest(req, benchmark);
        return repository.save(benchmark);
    }

    @Override
    public void delete(String benchmarkCode) {
        CtgCfgStatScoreBenchmark benchmark = repository.getByBenchmarkCode(benchmarkCode);
        if (benchmark == null) {
            throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        }
        repository.delete(benchmark);
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels, List<String> benchmarkCode, String fileName) {
        List<CtgCfgStatScoreBenchmark> data;
        if (benchmarkCode != null && !benchmarkCode.isEmpty()) {
            data = repository.findAll().stream()
                    .filter(statRegulatory -> benchmarkCode.contains(statRegulatory.getBenchmarkCode()))
                    .toList();
        } else {
            data = repository.findAll();
        }
        List<StatScoreBenchmarkExcel> excelData = mapper.toExcelDto(data);
        return excelService.exportToExcel(excelData, labels, StatScoreBenchmarkExcel.class, fileName);
    }

    @Override
    public List<CtgCfgStatScoreBenchmark> getByCode(String statScoreTypeCode) {
        return repository.findByStatScoreTypeCode(statScoreTypeCode);
    }
}
