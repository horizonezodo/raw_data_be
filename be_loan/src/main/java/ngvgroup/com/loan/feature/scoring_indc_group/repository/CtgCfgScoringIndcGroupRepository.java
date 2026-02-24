package ngvgroup.com.loan.feature.scoring_indc_group.repository;

import ngvgroup.com.loan.feature.scoring_indc_group.dto.CtgCfgScoringIndcGroupDtoV2;
import ngvgroup.com.loan.feature.scoring_indc_group.model.CtgCfgScoringIndcGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgScoringIndcGroupRepository extends JpaRepository<CtgCfgScoringIndcGroup,Long> {

    @Query("""
    SELECT new ngvgroup.com.loan.feature.scoring_indc_group.dto.CtgCfgScoringIndcGroupDtoV2(
    c.scoringIndcGroupCode,
    c.scoringIndcGroupName,
    c.sortNumber,
    c.scoringTypeCode,
    c.scoringIndcGroupType
)
FROM CtgCfgScoringIndcGroup c
WHERE c.scoringTypeCode = :scoringTypeCode
AND c.isActive = 1
AND  (:keyword IS NULL OR :keyword = '' OR
             LOWER(c.scoringIndcGroupName) LIKE CONCAT('%', LOWER(:keyword), '%') OR
             LOWER(c.scoringIndcGroupCode) LIKE CONCAT('%', LOWER(:keyword), '%'))
                                                                                           
""")
    Page<CtgCfgScoringIndcGroupDtoV2> getAllByTypeCode(@Param("scoringTypeCode") String scoringTypeCode, @Param("keyword")String keyword, Pageable pageable);


    @Query("""
    SELECT new ngvgroup.com.loan.feature.scoring_indc_group.dto.CtgCfgScoringIndcGroupDtoV2(
        c.scoringIndcGroupCode,
        c.scoringIndcGroupName,
        c.weightScore,
        c.sortNumber, null, null, null)
    FROM CtgCfgScoringIndcGroup c
    WHERE c.isActive=1 AND (:keyword IS NULL OR :keyword = '' 
           OR LOWER(c.scoringIndcGroupCode) LIKE CONCAT('%', LOWER(:keyword), '%')
           OR LOWER(c.scoringIndcGroupName) LIKE CONCAT('%', LOWER(:keyword), '%')
           OR TO_CHAR(c.weightScore) LIKE CONCAT('%', :keyword, '%')
        OR TO_CHAR(c.sortNumber) LIKE CONCAT('%', :keyword, '%')
           )
           ORDER BY c.modifiedDate DESC
""")
    Page<CtgCfgScoringIndcGroupDtoV2> searchAll(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
    SELECT new ngvgroup.com.loan.feature.scoring_indc_group.dto.CtgCfgScoringIndcGroupDtoV2(
        c.scoringIndcGroupCode,
        c.scoringIndcGroupName,
        c.weightScore,
        c.sortNumber, null, null, null)
    FROM CtgCfgScoringIndcGroup c
    WHERE c.isActive = 1 
      AND (
        :keyword IS NULL OR :keyword = '' 
        OR LOWER(c.scoringIndcGroupCode) LIKE CONCAT('%', LOWER(:keyword), '%')
        OR LOWER(c.scoringIndcGroupName) LIKE CONCAT('%', LOWER(:keyword), '%')
        OR TO_CHAR(c.weightScore) LIKE CONCAT('%', :keyword, '%')
        OR TO_CHAR(c.sortNumber) LIKE CONCAT('%', :keyword, '%')
      )
    ORDER BY c.modifiedDate DESC
""")
    List<CtgCfgScoringIndcGroupDtoV2> exportToExcel(@Param("keyword") String keyword);

    Optional<CtgCfgScoringIndcGroup> findCtgCfgScoringIndcGroupByScoringIndcGroupCode(@Param("scoringIndcGroupCode") String scoringIndcGroupCode);

    @Query("SELECT new ngvgroup.com.loan.feature.scoring_indc_group.dto.CtgCfgScoringIndcGroupDtoV2(" +
            "c.scoringIndcGroupCode," +
            "c.scoringIndcGroupName," +
            "c.weightScore," +
            "c.sortNumber," +
            "c.scoringTypeCode," +
            "c.scoringIndcGroupType," +
            "c.description) " +
            "FROM CtgCfgScoringIndcGroup c " +
            "WHERE c.scoringIndcGroupCode=:scoringIndcGroupCode")
    CtgCfgScoringIndcGroupDtoV2 getDetail(@Param("scoringIndcGroupCode") String scoringIndcGroupCode);


    List<CtgCfgScoringIndcGroup> getCtgCfgScoringIndcGroupByScoringTypeCode(@Param("scoringTypeCode") String scoringTypeCode);
}
