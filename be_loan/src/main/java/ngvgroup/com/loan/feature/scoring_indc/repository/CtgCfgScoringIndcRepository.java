package ngvgroup.com.loan.feature.scoring_indc.repository;

import ngvgroup.com.loan.feature.scoring_indc.dto.CtgCfgScoringIndcDto;
import ngvgroup.com.loan.feature.scoring_indc.model.CtgCfgScoringIndc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CtgCfgScoringIndcRepository extends JpaRepository<CtgCfgScoringIndc,Long> {
        @Query("""
    SELECT new ngvgroup.com.loan.feature.scoring_indc.dto.CtgCfgScoringIndcDto(
        s.indicatorCode,
        s.indicatorName,
        s.weightScore,
        c.commonName,
        CASE 
            WHEN s.dataType = 'Numberic' THEN 'Kiểu số' 
            WHEN s.dataType = 'Alpha' THEN 'Kiểu chữ' 
            WHEN s.dataType = 'Mix' THEN 'Kiểu chữ và kiểu số' 
            ELSE 'Không xác định'
        END
    )
    FROM CtgCfgScoringIndc s
    INNER JOIN CtgComCommon c ON s.controlType = c.commonCode
    WHERE s.isActive = 1 AND (
        (:keyword IS NULL OR :keyword = '' OR
        LOWER(s.indicatorCode) LIKE CONCAT('%', LOWER(:keyword), '%') OR
        LOWER(s.indicatorName) LIKE CONCAT('%', LOWER(:keyword), '%') OR
        LOWER(c.commonName) LIKE CONCAT('%', LOWER(:keyword), '%'))
        OR (:keywordNumber IS NOT NULL AND s.weightScore = :keywordNumber) OR 
        LOWER(
                    CASE\s
                        WHEN s.dataType = 'Numberic' THEN 'Kiểu số'\s
                        WHEN s.dataType = 'Alpha' THEN 'Kiểu chữ'\s
                        WHEN s.dataType = 'Mix' THEN 'Kiểu chữ và kiểu số'\s
                        ELSE 'Không xác định'
                    END
                ) LIKE CONCAT('%', LOWER(:keyword), '%')
    )
    ORDER BY s.modifiedDate DESC
""")
        Page<CtgCfgScoringIndcDto> findByKeyword(
                @Param("keyword") String keyword,
                @Param("keywordNumber") BigDecimal keywordNumber,
                Pageable pageable
        );


        @Query("""
        SELECT new ngvgroup.com.loan.feature.scoring_indc.dto.CtgCfgScoringIndcDto(
            s.indicatorCode,
            s.indicatorName,
            s.weightScore,
            c.commonName,
            CASE
                WHEN s.dataType = 'Numberic' THEN 'Kiểu số' 
                WHEN s.dataType = 'Alpha' THEN 'Kiểu chữ' 
                WHEN s.dataType = 'Mix' THEN 'Kiểu chữ và kiểu số' 
                ELSE 'Không xác định'
            END
        )
        FROM CtgCfgScoringIndc s
        INNER JOIN CtgComCommon c on s.controlType = c.commonCode
        WHERE
            (:keyword IS NULL OR :keyword = '' OR
             LOWER(s.indicatorCode) LIKE CONCAT('%', LOWER(:keyword), '%') OR
             LOWER(s.indicatorName) LIKE CONCAT('%', LOWER(:keyword), '%') OR
             LOWER(c.commonName) LIKE CONCAT('%', LOWER(:keyword), '%'))
    """)
        List<CtgCfgScoringIndcDto> exportExcelData(
                @Param("keyword") String keyword
        );

        boolean existsByIndicatorCode(String indicatorCode);

        CtgCfgScoringIndc findByIndicatorCode(String indicatorCode);

        @Query("""
        SELECT DISTINCT new ngvgroup.com.loan.feature.scoring_indc.dto.CtgCfgScoringIndcDto(
                                                                                   s.indicatorCode,
                                                                                   s.indicatorName,
                                                                                   CASE\s
                                                                                       WHEN ct.indicatorCode IS NOT NULL THEN true
                                                                                       ELSE false
                                                                                   END,
                                                                                   COALESCE(ct.sortNumber, 0)
                                                                               )
                                                                               FROM CtgCfgScoringIndc s
                                                                               LEFT JOIN CtgCfgScoringIndcMapp ct
                                                                                   ON s.indicatorCode = ct.indicatorCode
                                                                                  AND ct.scoringIndcGroupCode = :groupCode
                                                                               WHERE
                                                                                   s.isActive = 1
                                                                                   AND (
                                                                                       :keyword IS NULL OR :keyword = '' OR
                                                                                       LOWER(s.indicatorCode) LIKE CONCAT('%', LOWER(:keyword), '%') OR
                                                                                       LOWER(s.indicatorName) LIKE CONCAT('%', LOWER(:keyword), '%')
                                                                                   )
                                                                                   OR (
                                                                                       :keywordNumber IS NOT NULL
                                                                                       AND ct.sortNumber = :keywordNumber
                                                                                   )
                                                                               
    """)
        Page<CtgCfgScoringIndcDto> getPageData(
                @Param("groupCode") String groupCode,
                @Param("keywordNumber") BigDecimal keywordNumber,
                @Param("keyword") String keyword,
                Pageable pageable
        );
}
