package com.naas.admin_service.features.area_type.repository;

import jakarta.transaction.Transactional;
import com.naas.admin_service.features.area_type.dto.ComInfAreaTypeDto;
import com.naas.admin_service.features.area_type.model.ComInfAreaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComInfAreaTypeRepository extends JpaRepository<ComInfAreaType, Long> {



    @Query("SELECT new com.naas.admin_service.features.area_type.dto.ComInfAreaTypeDto(c.areaTypeCode,c.areaTypeName,c.description) " +
            "FROM ComInfAreaType c " +
            "WHERE c.isActive=1 " +
            "ORDER BY c.modifiedDate DESC ")
    Page<ComInfAreaTypeDto> getAreaTypes(Pageable pageable);




    @Query("SELECT new com.naas.admin_service.features.area_type.dto.ComInfAreaTypeDto(" +
            "c.areaTypeCode, c.areaTypeName, c.description) " +
            "FROM ComInfAreaType c " +
            "WHERE c.isActive = 1 " +
            "AND (" +
            "LOWER(c.areaTypeName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.areaTypeCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            ") " +
            "ORDER BY c.modifiedDate DESC")
    Page<ComInfAreaTypeDto> findAreaTypes(@Param("keyword") String keyword, Pageable pageable);

@Query("SELECT new com.naas.admin_service.features.area_type.dto.ComInfAreaTypeDto(" +
        "c.areaTypeCode, c.areaTypeName, c.description) " +
        "FROM ComInfAreaType c " +
        "WHERE c.isActive = 1 " +
        "ORDER BY c.modifiedDate DESC")
    List<ComInfAreaTypeDto> exportToExcel(@Param("keyword") String keyword);

    Optional<ComInfAreaType> findByAreaTypeCode(String areaTypeCode);

    @Modifying
    @Transactional
    @Query(value = "UPDATE COM_INF_AREA_TYPE SET " +
            "AREA_TYPE_NAME = :areaTypeName, " +
            "DESCRIPTION = :description, " +
            "MODIFIED_DATE = CURRENT_TIMESTAMP " +
            "WHERE AREA_TYPE_CODE = :areaTypeCode",
            nativeQuery = true)
    void updateAreaTypeCode(
            @Param("areaTypeCode") String areaTypeCode,
            @Param("areaTypeName") String areaTypeName,
            @Param("description") String description
    );


    @Query("SELECT new com.naas.admin_service.features.area_type.dto.ComInfAreaTypeDto(" +
            "ctg.areaTypeCode,ctg.areaTypeName,ctg.description) " +
            "FROM ComInfAreaType ctg " +
            "WHERE ctg.isActive=1 AND ctg.areaTypeCode=:areaTypeCode")
    ComInfAreaTypeDto getDetailAreaType(@Param("areaTypeCode") String areaTypeCode);

    void deleteCtgComAreaTypeByAreaTypeCode(String areaTypeCode);


    @Query("SELECT distinct new com.naas.admin_service.features.area_type.dto.ComInfAreaTypeDto(c.areaTypeCode,c.areaTypeName,c.description) " +
            "FROM ComInfAreaType c " +
            "WHERE c.isActive=1 ")
    List<ComInfAreaTypeDto> getDistinctAreaTypes();

}
