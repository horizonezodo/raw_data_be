package com.naas.category_service.repository;

import com.naas.category_service.dto.CtgInfIndustry.CtgInfIndustryResponse;
import com.naas.category_service.model.CtgInfIndustry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CtgInfIndustryRepository extends JpaRepository<CtgInfIndustry, Long> {
    @Query("""
    select new com.naas.category_service.dto.CtgInfIndustry.CtgInfIndustryResponse(
        cii.id, cii.industryCode, cii.industryName, cii.orgCode, null, cii.isActive)
    from CtgInfIndustry cii
    join ComInfOrganization c on c.orgCode = cii.orgCode
    where (:filter is null or 
        lower(cii.industryCode) like lower(concat('%', :filter, '%')) or
        lower(cii.industryName) like lower(concat('%', :filter, '%')) or
        lower(c.orgName) like lower(concat('%', :filter, '%')) or
        (lower(:filter) like LOWER('hiệu lực') and cii.isActive = true) or
        (lower(:filter) like LOWER('hết hiệu lực') and cii.isActive = false)
    )
    order by cii.modifiedDate desc
""")
    Page<CtgInfIndustryResponse> searchCtgInfIndustry(@Param("filter") String filter, Pageable pageable);


    @Query("""
    SELECT new com.naas.category_service.dto.CtgInfIndustry.CtgInfIndustryResponse(
        cii.id, cii.industryCode, cii.industryName, c.orgName, null, cii.isActive)
    FROM CtgInfIndustry cii
    JOIN ComInfOrganization c ON c.orgCode = cii.orgCode
    WHERE (:filter IS NULL OR (
        LOWER(cii.industryCode) LIKE LOWER(CONCAT('%', :filter, '%')) OR
        LOWER(cii.industryName) LIKE LOWER(CONCAT('%', :filter, '%')) OR
        LOWER(c.orgName) LIKE LOWER(CONCAT('%', :filter, '%')) OR
        (LOWER(:filter) LIKE LOWER('hiệu lực') AND cii.isActive = true) OR
        (LOWER(:filter) LIKE LOWER('hết hiệu lực') AND cii.isActive = false)
    ))
    ORDER BY cii.modifiedDate DESC
""")
    List<CtgInfIndustryResponse> exportToExcel(@Param("filter") String filter);




    List<CtgInfIndustry> findCtgInfIndustrysByIndustryCodeAndIsDelete(String industryCode, int isDelete);

    Optional<CtgInfIndustry> findByIndustryCode(String industryCode);

    @Modifying
    @Transactional
    @Query("UPDATE CtgInfIndustry c SET c.isActive=:isActive,c.orgCode=:orgCode,c.industryName=:industryName,c.description=:description,c.modifiedDate=CURRENT_TIMESTAMP " +
            "WHERE c.industryCode=:industryCode")
    void updateCtgInfIndustry(@Param("isActive") Boolean isActive,
                              @Param("industryCode") String industryCode,
                              @Param("orgCode") String orgCode,
                              @Param("industryName") String industryName,
                              @Param("description") String description);

}
