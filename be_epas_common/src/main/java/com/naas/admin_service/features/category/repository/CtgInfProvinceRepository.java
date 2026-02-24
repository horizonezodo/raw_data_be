package com.naas.admin_service.features.category.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.naas.admin_service.features.category.dto.CtgInfProvinceDto;
import com.naas.admin_service.features.category.model.CtgInfProvince;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgInfProvinceRepository extends JpaRepository<CtgInfProvince, Long> {

    @Query("SELECT new com.naas.admin_service.features.category.dto.CtgInfProvinceDto(" +
            "c.provinceCode,c.provinceName,c.taxCode) " +
            "FROM CtgInfProvince c " +
            "WHERE c.isActive=1 " +
            "ORDER BY c.provinceCode ASC ")
    Page<CtgInfProvinceDto> getProvinces(Pageable pageable);

    @Query("SELECT new com.naas.admin_service.features.category.dto.CtgInfProvinceDto(" +
            "c.provinceCode, c.provinceName, c.taxCode,c.description) " +
            "FROM CtgInfProvince c " +
            "WHERE c.isActive = 1 " +
            "AND (:keyword IS NULL OR " +
            "LOWER(c.provinceCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.provinceName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.taxCode) LIKE LOWER(CONCAT('%', :keyword, '%')) ) " +
            "ORDER BY c.provinceCode ASC")
    Page<CtgInfProvinceDto> findProvinces(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT new com.naas.admin_service.features.category.dto.CtgInfProvinceDto(" +
            "c.provinceCode, c.provinceName, c.taxCode) " +
            "FROM CtgInfProvince c " +
            "WHERE c.isActive = 1 " +
            "AND (:keyword IS NULL OR " +
            "LOWER(c.provinceCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.provinceName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.taxCode) LIKE LOWER(CONCAT('%', :keyword, '%')) ) " +
            "ORDER BY c.provinceCode ASC")
    List<CtgInfProvinceDto> exportToExcel(@Param("keyword") String keyword);

    @Query("SELECT new com.naas.admin_service.features.category.dto.CtgInfProvinceDto(" +
            "c.provinceCode,c.provinceName,c.taxCode,c.description) " +
            "FROM CtgInfProvince c " +
            "WHERE c.isActive=1 " +
            "AND c.provinceCode=:provinceCode")
    CtgInfProvinceDto getDetailProvince(@Param("provinceCode") String provinceCode);


    void deleteCtgInfProvinceByProvinceCode(String provinceCode);

    @Modifying
    @Transactional
    @Query("UPDATE CtgInfProvince c SET c.provinceName = :provinceName, c.taxCode = :taxCode, c.description = :description,c.modifiedDate=CURRENT_TIMESTAMP " +
            "WHERE c.provinceCode = :provinceCode")
    void updateProvince(@Param("provinceCode") String provinceCode,
                        @Param("provinceName") String provinceName,
                        @Param("taxCode") String taxCode,
                        @Param("description") String description);

    Optional<CtgInfProvince> findByProvinceCode(String provinceCode);

    @Query("SELECT new com.naas.admin_service.features.category.dto.CtgInfProvinceDto(" +
            "c.provinceCode,c.provinceName) " +
            "FROM CtgInfProvince c")
    List<CtgInfProvinceDto> getAll();
}
