package ngvgroup.com.rpt.features.ctgcfgstat.repository;

import ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatscorekpiresult.CtgCfgStatScoreKpiResultDto;
import ngvgroup.com.rpt.features.ctgcfgstat.model.CtgCfgStatScoreKpiResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgStatScoreKpiResultRepository extends JpaRepository<CtgCfgStatScoreKpiResult,Long> {

    @Query("""
SELECT new ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatscorekpiresult.CtgCfgStatScoreKpiResultDto(
    c.id,
    c.resultName,
    c.scoreValue,
    c.scoreValueMax,
    c.scoreValueMin,
    c.conditionExpression,
    c.sortNumber
)
FROM CtgCfgStatScoreKpiResult c
WHERE c.isDelete = 0 AND c.kpiCode=:kpiCode AND(
    :keyword IS NULL
    OR LOWER(c.resultName) LIKE LOWER(CONCAT('%', :keyword, '%'))
    OR STR(c.scoreValue) LIKE CONCAT('%', :keyword, '%')
    OR STR(c.scoreValueMin) LIKE CONCAT('%', :keyword, '%')
    OR STR(c.scoreValueMax) LIKE CONCAT('%', :keyword, '%')
    OR LOWER(c.conditionExpression) LIKE LOWER(CONCAT('%', :keyword, '%'))
    OR STR(c.sortNumber) LIKE CONCAT('%', :keyword, '%')
)
ORDER BY c.sortNumber
""")
    Page<CtgCfgStatScoreKpiResultDto> searchAll(@Param("kpiCode")String kpiCode,@Param("keyword") String keyword, Pageable pageable);

    List<CtgCfgStatScoreKpiResult> getAllByKpiCode(String kpiCode);

    void deleteAllByKpiCode(String kpiCode);

}
