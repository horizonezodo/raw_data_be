package ngvgroup.com.loan.feature.scoring_benchmark.service;

import ngvgroup.com.loan.feature.scoring_benchmark.dto.CtgCfgScoringBenchmarkDTO;
import ngvgroup.com.loan.feature.scoring_benchmark.dto.ListCtgCfgScoringBenchmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CtgCfgScoringBenchmarkService {
    Page<ListCtgCfgScoringBenchmark> pageScoringBenchmark(String keyword, Pageable pageable);

    void createScoringBenchmark(CtgCfgScoringBenchmarkDTO dto);

    void updateScoringBenchmark(CtgCfgScoringBenchmarkDTO dto, String benchmarkCode);

    void deleteScoringBenchmark(String benchmarkCode);

    ResponseEntity<byte[]> exportToExcel(CtgCfgScoringBenchmarkDTO dto, String keyword, String fileName);

    CtgCfgScoringBenchmarkDTO getOne(String benchmarkCode);

    boolean existByBenchmarkCode(String benchmarkCode);
}
