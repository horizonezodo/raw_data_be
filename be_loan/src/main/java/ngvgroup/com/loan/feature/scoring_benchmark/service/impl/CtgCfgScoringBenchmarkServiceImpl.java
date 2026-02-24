package ngvgroup.com.loan.feature.scoring_benchmark.service.impl;

import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.core.constant.LoanErrorCode;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.feature.scoring_benchmark.dto.CtgCfgScoringBenchmarkDTO;
import ngvgroup.com.loan.feature.scoring_benchmark.dto.ListCtgCfgScoringBenchmark;
import ngvgroup.com.loan.feature.scoring_benchmark.model.CtgCfgScoringBenchmark;
import ngvgroup.com.loan.feature.scoring_benchmark.repository.CtgCfgScoringBenchmarkRepository;
import ngvgroup.com.loan.feature.scoring_benchmark.service.CtgCfgScoringBenchmarkService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CtgCfgScoringBenchmarkServiceImpl extends BaseStoredProcedureService implements CtgCfgScoringBenchmarkService {
    private final CtgCfgScoringBenchmarkRepository ctgCfgScoringBenchmarkRepository;
    private final ExportExcel exportExcel;

    @Override
    public Page<ListCtgCfgScoringBenchmark> pageScoringBenchmark(String keyword, Pageable pageable) {
        BigDecimal keywordNumber;
        try {
            keywordNumber = new BigDecimal(keyword);
        } catch (NumberFormatException ignored) {
            keywordNumber = null;
        }
        return ctgCfgScoringBenchmarkRepository.findByKeyword(keyword,keywordNumber, pageable);
    }

    @Override
    public void createScoringBenchmark(CtgCfgScoringBenchmarkDTO dto) {
        CtgCfgScoringBenchmark findCtg = ctgCfgScoringBenchmarkRepository.findByBenchmarkCode(dto.getBenchmarkCode());
        if(findCtg != null) throw new BusinessException(ErrorCode.CONFLICT, "Đã tồn tại điểm chuẩn code: " + dto.getBenchmarkCode());
        CtgCfgScoringBenchmark ctg = new CtgCfgScoringBenchmark();
        BeanUtils.copyProperties(dto, ctg);
        ctg.setCreatedBy(getCurrentUserName());
        ctg.setIsActive(1);
        ctg.setIsDelete(0);
        ctg.setRecordStatus(LoanVariableConstants.APPROVAL);
        ctg.setModifiedBy(getCurrentUserName());
        ctg.setApprovedBy(getCurrentUserName());
        ctgCfgScoringBenchmarkRepository.save(ctg);
    }

    @Override
    public void updateScoringBenchmark(CtgCfgScoringBenchmarkDTO dto, String benchmarkCode) {
        CtgCfgScoringBenchmark findCtg = ctgCfgScoringBenchmarkRepository.findByBenchmarkCode(benchmarkCode);
        if(findCtg != null){
            BeanUtils.copyProperties(dto,findCtg);
            findCtg.setModifiedBy(getCurrentUserName());
            ctgCfgScoringBenchmarkRepository.save(findCtg);
        }else{
            throw new BusinessException(LoanErrorCode.SCORING_BENCH_MARK_ERROR, benchmarkCode);
        }
    }

    @Override
    public void deleteScoringBenchmark(String benchmarkCode) {
        CtgCfgScoringBenchmark findCtg = ctgCfgScoringBenchmarkRepository.findByBenchmarkCode(benchmarkCode);
        if(findCtg != null){
            ctgCfgScoringBenchmarkRepository.delete(findCtg);
        }else{
            throw new BusinessException(LoanErrorCode.SCORING_BENCH_MARK_ERROR, benchmarkCode);
        }
    }

    @Override
    public ResponseEntity<byte[]> exportToExcel(CtgCfgScoringBenchmarkDTO dto, String keyword, String fileName) {
        try {
            List<ListCtgCfgScoringBenchmark> lst = ctgCfgScoringBenchmarkRepository.listScroringBenchmark(keyword);

            return exportExcel.exportExcel(lst, fileName);
        } catch (Exception e) {
            throw new BusinessException(LoanErrorCode.WRITE_EXCEL_ERROR, e);
        }
    }

    @Override
    public CtgCfgScoringBenchmarkDTO getOne(String benchmarkCode) {
        CtgCfgScoringBenchmark findCtg = ctgCfgScoringBenchmarkRepository.findByBenchmarkCode(benchmarkCode);
        if(findCtg != null){
            CtgCfgScoringBenchmarkDTO dto = new CtgCfgScoringBenchmarkDTO();
            BeanUtils.copyProperties(findCtg,dto);
            dto.setDesc(findCtg.getDescription());
            return dto;
        }else{
            throw new BusinessException(ErrorCode.NOT_FOUND, "Không tìm thấy điểm chuẩn với code: " + benchmarkCode);
        }
    }

    @Override
    public boolean existByBenchmarkCode(String benchmarkCode) {
        return ctgCfgScoringBenchmarkRepository.existsByBenchmarkCode(benchmarkCode);
    }
}
