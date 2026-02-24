package com.naas.admin_service.features.category.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.naas.admin_service.features.category.dto.CtgInfEconomicTypeResponse;
import com.naas.admin_service.features.category.model.CtgInfEconomicType;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgInfEconomicTypeRepository extends JpaRepository<CtgInfEconomicType, Long> {
    @Query("""
    SELECT new com.naas.admin_service.features.category.dto.CtgInfEconomicTypeResponse(
        ciet.id, ciet.economicTypeCode, ciet.economicTypeName,
        c.orgName, null, ciet.isActive, ciet.orgCode)
    FROM CtgInfEconomicType ciet
    LEFT JOIN ComInfOrganization c ON ciet.orgCode = c.orgCode
    WHERE (:filter IS NULL OR (
        LOWER(ciet.economicTypeCode) LIKE LOWER(CONCAT('%', :filter, '%')) OR
        LOWER(ciet.economicTypeName) LIKE LOWER(CONCAT('%', :filter, '%')) OR
        LOWER(c.orgName) LIKE LOWER(CONCAT('%', :filter, '%')) OR
        (LOWER(:filter) LIKE LOWER('hiệu lực') AND ciet.isActive = 1) OR
        (LOWER(:filter) LIKE LOWER('hết hiệu lực') AND ciet.isActive = 0)
    ))
    ORDER BY ciet.modifiedDate DESC
""")
    Page<CtgInfEconomicTypeResponse> searchCtgInfEconomicType(@Param("filter") String filter, Pageable pageable);

@Query("""
SELECT new com.naas.admin_service.features.category.dto.CtgInfEconomicTypeResponse(
       ciet.economicTypeCode, ciet.economicTypeName
        )
    FROM CtgInfEconomicType ciet
""")
    List<CtgInfEconomicTypeResponse> getAllData();


    @Query("""
    SELECT new com.naas.admin_service.features.category.dto.CtgInfEconomicTypeResponse(
        ciet.id,
        ciet.economicTypeCode,
        ciet.economicTypeName,
        c.orgName,
        null,
        ciet.isActive,
        ciet.orgCode
    )
    FROM CtgInfEconomicType ciet
    LEFT JOIN ComInfOrganization c ON ciet.orgCode = c.orgCode
    WHERE :filter IS NULL OR (
        LOWER(ciet.economicTypeCode) LIKE LOWER(CONCAT('%', :filter, '%')) OR
        LOWER(ciet.economicTypeName) LIKE LOWER(CONCAT('%', :filter, '%')) OR
        LOWER(c.orgName) LIKE LOWER(CONCAT('%', :filter, '%')) OR
        LOWER(ciet.orgCode) LIKE LOWER(CONCAT('%', :filter, '%')) OR
        (LOWER(:filter) LIKE LOWER('hiệu lực') AND ciet.isActive = 1) OR
        (LOWER(:filter) LIKE LOWER('hết hiệu lực') AND ciet.isActive = 0)
    )
    ORDER BY ciet.modifiedDate DESC
""")
    List<CtgInfEconomicTypeResponse> exportToExcel(@Param("filter") String filter);

    List<CtgInfEconomicType> findCtgInfEconomicTypesByEconomicTypeCodeAndIsDelete(String economicTypeCode, int isDelete);

    Optional<CtgInfEconomicType> findByEconomicTypeCode(String economicTypeCode);

    @Modifying
    @Transactional
    @Query("UPDATE CtgInfEconomicType c SET c.isActive=:isActive,c.orgCode=:orgCode,c.economicTypeName=:economicTypeName,c.description=:description,c.modifiedDate=CURRENT_TIMESTAMP " +
            "WHERE c.economicTypeCode=:economicTypeCode")
    void updateEconomicType(@Param("isActive") Integer isActive,
                            @Param("orgCode") String orgCode,
                            @Param("economicTypeCode") String economicTypeCode,
                            @Param("economicTypeName") String economicTypeName,
                            @Param("description") String description );

}
