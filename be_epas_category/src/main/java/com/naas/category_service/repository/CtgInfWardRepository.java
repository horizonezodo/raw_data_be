package com.naas.category_service.repository;

import com.naas.category_service.dto.CtgInfWard.CtgInfWardDto;
import com.naas.category_service.model.CtgInfWard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgInfWardRepository extends JpaRepository<CtgInfWard, Long> {

    @Query("SELECT new com.naas.category_service.dto.CtgInfWard.CtgInfWardDto(" +
            "c.wardCode,c.wardName,c.provinceCode,p.provinceName) " +
            "FROM CtgInfWard c " +
            "JOIN CtgInfProvince p ON c.provinceCode=p.provinceCode " +
            "WHERE c.isActive=true " +
            "ORDER BY c.wardCode ASC ")
    Page<CtgInfWardDto> getWards(Pageable pageable);

    @Query("SELECT new com.naas.category_service.dto.CtgInfWard.CtgInfWardDto(" +
            "c.wardCode, c.wardName, c.provinceCode, p.provinceName,p.description) " +
            "FROM CtgInfWard c " +
            "JOIN CtgInfProvince p ON c.provinceCode = p.provinceCode " +
            "WHERE c.isActive = true " +
            "AND (:keyword IS NULL OR " +
            "LOWER(c.wardCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.wardName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.provinceCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.provinceName) LIKE LOWER(CONCAT('%', :keyword, '%')) ) " +
            "ORDER BY c.wardCode ASC")
    Page<CtgInfWardDto> searchWards(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT new com.naas.category_service.dto.CtgInfWard.CtgInfWardDto(" +
            "c.wardCode, c.wardName, c.provinceCode, p.provinceName) " +
            "FROM CtgInfWard c " +
            "JOIN CtgInfProvince p ON c.provinceCode = p.provinceCode " +
            "WHERE c.isActive = true " +
            "AND (:keyword IS NULL OR " +
            "LOWER(c.wardCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.wardName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.provinceCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.provinceName) LIKE LOWER(CONCAT('%', :keyword, '%')) ) " +
            "ORDER BY c.wardCode ASC")
    List<CtgInfWardDto> exportToExcel(@Param("keyword") String keyword);

    @Query("SELECT new com.naas.category_service.dto.CtgInfWard.CtgInfWardDto(" +
            "c.wardCode, c.wardName, c.provinceCode, null,c.description) " +
            "FROM CtgInfWard c " +
            "WHERE c.isActive=true " +
            "AND c.wardCode=:wardCode")
    CtgInfWardDto getDetail(@Param("wardCode") String wardCode);

    @Modifying
    @Transactional
    @Query("UPDATE CtgInfWard c SET c.wardName = :wardName, c.provinceCode = :provinceCode, c.description = :description,c.modifiedDate=CURRENT_TIMESTAMP " +
            "WHERE c.wardCode = :wardCode")
    void updateWard(
            @Param("wardCode") String wardCode,
            @Param("wardName") String wardName,
            @Param("provinceCode") String provinceCode,
            @Param("description") String description
    );



    void deleteCtgInfWardByWardCode(String wardCode);


    Optional<CtgInfWard> findByWardCode(String wardCode);
}
