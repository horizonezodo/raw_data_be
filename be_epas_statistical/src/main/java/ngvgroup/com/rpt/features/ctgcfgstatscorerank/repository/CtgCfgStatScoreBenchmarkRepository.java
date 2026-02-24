package ngvgroup.com.rpt.features.ctgcfgstatscorerank.repository;

import feign.Param;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.model.CtgCfgStatScoreBenchmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgStatScoreBenchmarkRepository extends JpaRepository<CtgCfgStatScoreBenchmark, Long>, JpaSpecificationExecutor<CtgCfgStatScoreBenchmark> {
    boolean existsByBenchmarkCode(String benchmarkCode);
    CtgCfgStatScoreBenchmark getByBenchmarkCode(String benchmarkCode);
    List<CtgCfgStatScoreBenchmark> findByStatScoreTypeCode(String statScoreTypeCode);

    @Query("SELECT b FROM CtgCfgStatScoreBenchmark b WHERE b.statScoreTypeCode = :typeCode AND :score BETWEEN b.scoreValueMin AND b.scoreValueMax")
    Optional<CtgCfgStatScoreBenchmark> findBenchmark(@Param("typeCode") String typeCode, @Param("score") BigDecimal score);
}
