package ngvgroup.com.loan.feature.scoring_benchmark.repository;

import ngvgroup.com.loan.feature.scoring_benchmark.dto.ListCtgCfgScoringBenchmark;
import ngvgroup.com.loan.feature.scoring_benchmark.model.CtgCfgScoringBenchmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CtgCfgScoringBenchmarkRepository extends JpaRepository<CtgCfgScoringBenchmark,Long> {

    @Query(value = """
    SELECT new ngvgroup.com.loan.feature.scoring_benchmark.dto.ListCtgCfgScoringBenchmark (
        c.benchmarkCode,
        c.benchmarkName,
        ct.scoringTypeName,
        c.benchmarkValue,
        c.scoreValueMin,
        c.scoreValueMax,
        c.benchmarkDesc
    )
    FROM CtgCfgScoringBenchmark c
    LEFT JOIN CtgCfgScoringType  ct
    ON c.scoringTypeCode = ct.scoringTypeCode
    WHERE c.isActive = 1
    AND  (:keyword IS NULL OR :keyword = '' OR
             LOWER(c.benchmarkCode) LIKE CONCAT('%', LOWER(:keyword), '%') OR
             LOWER(c.benchmarkName) LIKE CONCAT('%', LOWER(:keyword), '%') OR
             LOWER(c.benchmarkValue) LIKE CONCAT('%', LOWER(:keyword), '%') OR
             LOWER(c.benchmarkDesc) LIKE CONCAT('%', LOWER(:keyword), '%') OR
             LOWER(ct.scoringTypeName) LIKE CONCAT('%', LOWER(:keyword), '%'))
              OR (:keywordNumber IS NOT NULL AND c.scoreValueMin = :keywordNumber)
               OR (:keywordNumber IS NOT NULL AND c.scoreValueMax = :keywordNumber)
    ORDER BY c.modifiedDate DESC               
    """
            )
    Page<ListCtgCfgScoringBenchmark> findByKeyword(@Param("keyword") String keyword, @Param("keywordNumber") BigDecimal keywordNumber, Pageable pageable);



    @Query(value ="""
        SELECT new ngvgroup.com.loan.feature.scoring_benchmark.dto.ListCtgCfgScoringBenchmark (
        c.benchmarkCode,
        c.benchmarkName,
        ct.scoringTypeName,
        c.benchmarkValue,
        c.scoreValueMin,
        c.scoreValueMax,
        c.benchmarkDesc
    )
    FROM CtgCfgScoringBenchmark c
    LEFT JOIN CtgCfgScoringType  ct
    ON c.scoringTypeCode = ct.scoringTypeCode
    WHERE c.isActive = 1
    AND  (:keyword IS NULL OR :keyword = '' OR
             LOWER(c.benchmarkCode) LIKE CONCAT('%', LOWER(:keyword), '%') OR
             LOWER(c.benchmarkName) LIKE CONCAT('%', LOWER(:keyword), '%') OR
             LOWER(c.benchmarkValue) LIKE CONCAT('%', LOWER(:keyword), '%') OR
             LOWER(c.benchmarkDesc) LIKE CONCAT('%', LOWER(:keyword), '%') OR
             LOWER(ct.scoringTypeName) LIKE CONCAT('%', LOWER(:keyword), '%'))
""")
    List<ListCtgCfgScoringBenchmark> listScroringBenchmark(@Param("keyword")String keyword);

    CtgCfgScoringBenchmark findByBenchmarkCode(String benchmarkCode);

    boolean existsByBenchmarkCode(String benchmarkCode);

    List<CtgCfgScoringBenchmark> getCtgCfgScoringBenchmarkByScoringTypeCode(@Param("scoringTypeCode") String scoringTypeCode);
}
