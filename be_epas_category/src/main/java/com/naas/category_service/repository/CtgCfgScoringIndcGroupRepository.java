package com.naas.category_service.repository;

import com.naas.category_service.dto.CtgCfgScoringIndcGroup.CtgCfgScoringIndcGroupDto;
import com.naas.category_service.model.CtgCfgScoringIndcGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgScoringIndcGroupRepository  extends JpaRepository<CtgCfgScoringIndcGroup, Long> {

    @Query("""
    SELECT new com.naas.category_service.dto.CtgCfgScoringIndcGroup.CtgCfgScoringIndcGroupDto(
        c.scoringIndcGroupCode,
        c.scoringIndcGroupName,
        c.weightScore,
        c.sortNumber, null, null, null)
    FROM CtgCfgScoringIndcGroup c
    WHERE c.isActive=true AND (:keyword IS NULL OR :keyword = '' 
           OR LOWER(c.scoringIndcGroupCode) LIKE CONCAT('%', LOWER(:keyword), '%')
           OR LOWER(c.scoringIndcGroupName) LIKE CONCAT('%', LOWER(:keyword), '%')
           OR TO_CHAR(c.weightScore) LIKE CONCAT('%', :keyword, '%')
        OR TO_CHAR(c.sortNumber) LIKE CONCAT('%', :keyword, '%')
           )
           ORDER BY c.modifiedDate DESC
""")
    Page<CtgCfgScoringIndcGroupDto> searchAll(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
    SELECT new com.naas.category_service.dto.CtgCfgScoringIndcGroup.CtgCfgScoringIndcGroupDto(
        c.scoringIndcGroupCode,
        c.scoringIndcGroupName,
        c.weightScore,
        c.sortNumber, null, null, null)
    FROM CtgCfgScoringIndcGroup c
    WHERE c.isActive = true 
      AND (
        :keyword IS NULL OR :keyword = '' 
        OR LOWER(c.scoringIndcGroupCode) LIKE CONCAT('%', LOWER(:keyword), '%')
        OR LOWER(c.scoringIndcGroupName) LIKE CONCAT('%', LOWER(:keyword), '%')
        OR TO_CHAR(c.weightScore) LIKE CONCAT('%', :keyword, '%')
        OR TO_CHAR(c.sortNumber) LIKE CONCAT('%', :keyword, '%')
      )
    ORDER BY c.modifiedDate DESC
""")

    List<CtgCfgScoringIndcGroupDto> exportToExcel(@Param("keyword") String keyword);

    Optional<CtgCfgScoringIndcGroup> findCtgCfgScoringIndcGroupByScoringIndcGroupCode(@Param("scoringIndcGroupCode") String scoringIndcGroupCode);

    @Query("SELECT new com.naas.category_service.dto.CtgCfgScoringIndcGroup.CtgCfgScoringIndcGroupDto(" +
            "c.scoringIndcGroupCode," +
            "c.scoringIndcGroupName," +
            "c.weightScore," +
            "c.sortNumber," +
            "c.scoringTypeCode," +
            "c.scoringIndcGroupType," +
            "c.description) " +
            "FROM CtgCfgScoringIndcGroup c " +
            "WHERE c.scoringIndcGroupCode=:scoringIndcGroupCode")
    CtgCfgScoringIndcGroupDto getDetail(@Param("scoringIndcGroupCode") String scoringIndcGroupCode);


    List<CtgCfgScoringIndcGroup> getCtgCfgScoringIndcGroupByScoringTypeCode(@Param("scoringTypeCode") String scoringTypeCode);
}
