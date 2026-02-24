package ngvgroup.com.rpt.features.dim.repository;

import ngvgroup.com.rpt.features.dim.dto.DimCiBrDDTO;
import ngvgroup.com.rpt.features.dim.model.DimCiBrD;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface DimCiBrDRepository extends JpaRepository<DimCiBrD,Long> {

    @Query("""
        SELECT new ngvgroup.com.rpt.features.dim.dto.DimCiBrDDTO(
            d.ciBrCode,
            d.ciBrName,
            s.achievedScore,
            s.rankValue
        )FROM DimCiBrD d
        LEFT JOIN SmrTxnScoreBranch s
        ON d.ciId = s.ciId
        LEFT JOIN SmrTxnScore sm
        ON d.ciId = sm.ciId
        WHERE d.isActive = 1
        AND sm.scoreInstanceCode=:scoreInstanceCode
        AND (
            :keyword IS NULL OR 
            LOWER(d.ciBrCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(d.ciBrName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(s.rankValue) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            (:keywordNumeric IS NOT NULL AND s.achievedScore = :keywordNumeric)
        )
    """)
    Page<DimCiBrDDTO> pageDimCiBr(@Param("keyword")String keyword,@Param("scoreInstanceCode")String scoreInstanceCode, @Param("keywordNumeric") BigDecimal keywordNumeric, Pageable pageable);
}
