package ngvgroup.com.loan.feature.scoring_type.repository;

import ngvgroup.com.loan.feature.scoring_type.dto.CtgCfgScoringTypeDTO;
import ngvgroup.com.loan.feature.scoring_type.model.CtgCfgScoringType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgScoringTypeRepository extends JpaRepository<CtgCfgScoringType,Long> {
    @Query("""
    SELECT new ngvgroup.com.loan.feature.scoring_type.dto.CtgCfgScoringTypeDTO(
        c.scoringTypeCode,
        c.scoringTypeName
    )
    FROM CtgCfgScoringType c
    WHERE c.isActive = 1
""")
    List<CtgCfgScoringTypeDTO> getAllData();

    @Query("""
    SELECT new ngvgroup.com.loan.feature.scoring_type.dto.CtgCfgScoringTypeDTO(
        c.scoringTypeCode,
        c.scoringTypeName
    )
    FROM CtgCfgScoringType c
    WHERE c.isActive = 1
   AND  (:keyword IS NULL OR :keyword = '' OR
             LOWER( c.scoringTypeCode) LIKE CONCAT('%', LOWER(:keyword), '%') OR
             LOWER(c.scoringTypeName) LIKE CONCAT('%', LOWER(:keyword), '%'))
""")
    Page<CtgCfgScoringTypeDTO> pageData(@Param("keyword")String keyword, Pageable pageable);



    @Query("SELECT new ngvgroup.com.loan.feature.scoring_type.dto.CtgCfgScoringTypeDTO(" +
            "c.scoringTypeCode,c.scoringTypeName) " +
            "FROM CtgCfgScoringType c")
    List<CtgCfgScoringTypeDTO> getAll();


    @Query("SELECT new ngvgroup.com.loan.feature.scoring_type.dto.CtgCfgScoringTypeDTO(" +
            "c.scoringTypeCode,c.scoringTypeName,c.description) " +
            "FROM CtgCfgScoringType c " +
            "WHERE :keyword IS NULL OR :keyword='' OR " +
            "LOWER(c.scoringTypeCode) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(c.scoringTypeName) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(c.description) LIKE CONCAT('%', LOWER(:keyword), '%')" +
            "ORDER BY c.modifiedDate DESC ")
    Page<CtgCfgScoringTypeDTO> searchAll(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT new ngvgroup.com.loan.feature.scoring_type.dto.CtgCfgScoringTypeDTO(" +
            "c.scoringTypeCode,c.scoringTypeName,c.description) " +
            "FROM CtgCfgScoringType c " +
            "WHERE :keyword IS NULL OR :keyword='' OR " +
            "LOWER(c.scoringTypeCode) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(c.scoringTypeName) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(c.description) LIKE CONCAT('%', LOWER(:keyword), '%')" +
            "ORDER BY c.modifiedDate DESC ")
    List<CtgCfgScoringTypeDTO> exportToExcel(@Param("keyword") String keyword);

    Optional<CtgCfgScoringType> findCtgCfgScoringTypeByScoringTypeCode(String scoringTypeCode);

    @Query("SELECT new ngvgroup.com.loan.feature.scoring_type.dto.CtgCfgScoringTypeDTO(" +
            "c.scoringTypeCode,c.scoringTypeName," +
            "c.description,c.sqlDataCollection," +
            "c.templateCollectionCode,c.sqlCalcResult,c.templateResultCode) " +
            "FROM CtgCfgScoringType c " +
            "WHERE c.scoringTypeCode=:scoringTypeCode")
    CtgCfgScoringTypeDTO getDetail(@Param("scoringTypeCode") String scoringTypeCode);
}
