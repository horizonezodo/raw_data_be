package com.naas.category_service.repository;

import com.naas.category_service.dto.CtgCfgScoringType.CtgCfgScoringTypeDto;
import com.naas.category_service.model.CtgCfgScoringType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgScoringTypeRepository extends JpaRepository<CtgCfgScoringType, Long> {

    @Query("SELECT new com.naas.category_service.dto.CtgCfgScoringType.CtgCfgScoringTypeDto(" +
            "c.scoringTypeCode,c.scoringTypeName) " +
            "FROM CtgCfgScoringType c")
    List<CtgCfgScoringTypeDto> getAll();


    @Query("SELECT new com.naas.category_service.dto.CtgCfgScoringType.CtgCfgScoringTypeDto(" +
            "c.scoringTypeCode,c.scoringTypeName,c.description) " +
            "FROM CtgCfgScoringType c " +
            "WHERE :keyword IS NULL OR :keyword='' OR " +
            "LOWER(c.scoringTypeCode) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(c.scoringTypeName) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(c.description) LIKE CONCAT('%', LOWER(:keyword), '%')" +
            "ORDER BY c.modifiedDate DESC ")
    Page<CtgCfgScoringTypeDto> searchAll(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT new com.naas.category_service.dto.CtgCfgScoringType.CtgCfgScoringTypeDto(" +
            "c.scoringTypeCode,c.scoringTypeName,c.description) " +
            "FROM CtgCfgScoringType c " +
            "WHERE :keyword IS NULL OR :keyword='' OR " +
            "LOWER(c.scoringTypeCode) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(c.scoringTypeName) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(c.description) LIKE CONCAT('%', LOWER(:keyword), '%')" +
            "ORDER BY c.modifiedDate DESC ")
    List<CtgCfgScoringTypeDto> exportToExcel(@Param("keyword") String keyword);

    Optional<CtgCfgScoringType> findCtgCfgScoringTypeByScoringTypeCode(String scoringTypeCode);

    @Query("SELECT new com.naas.category_service.dto.CtgCfgScoringType.CtgCfgScoringTypeDto(" +
            "c.scoringTypeCode,c.scoringTypeName," +
            "c.description,c.sqlDataCollection," +
            "c.templateCollectionCode,c.sqlCalcResult,c.templateResultCode) " +
            "FROM CtgCfgScoringType c " +
            "WHERE c.scoringTypeCode=:scoringTypeCode")
    CtgCfgScoringTypeDto getDetail(@Param("scoringTypeCode") String scoringTypeCode);
}
