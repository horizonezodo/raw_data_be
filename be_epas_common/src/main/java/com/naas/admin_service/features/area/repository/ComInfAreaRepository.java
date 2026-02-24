package com.naas.admin_service.features.area.repository;

import com.naas.admin_service.features.category.dto.CtgInfWardDto;
import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import com.naas.admin_service.features.area.dto.ComInfAreaDto;
import com.naas.admin_service.features.area.dto.ComInfAreaResponse;
import com.naas.admin_service.features.area.model.ComInfArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComInfAreaRepository extends BaseRepository<ComInfArea> {

    @Query("select new com.naas.admin_service.features.area.dto.ComInfAreaDto(" +
            "c.areaCode, c.areaName) " +
            "FROM ComInfArea c " +
            "WHERE (:keyword IS NULL OR LOWER(c.areaCode) LIKE %:keyword% OR LOWER(c.areaName) LIKE %:keyword%)\n" +
            "AND c.orgCode = :orgCode")
    Page<ComInfAreaDto> getList(@Param("keyword") String keyword, @Param("orgCode") String orgCode, Pageable pageable);

    @Query("SELECT new com.naas.admin_service.features.area.dto.ComInfAreaResponse(" +
            "cca.id, cca.areaCode, cca.areaName, cio.orgName, ccat.areaTypeName, ccw.wardName) " +
            "FROM ComInfArea cca " +
            "JOIN ComInfAreaType ccat ON cca.areaTypeCode = ccat.areaTypeCode " +
            "JOIN CtgInfWard ccw ON cca.wardCode = ccw.wardCode " +
            "JOIN ComInfOrganization cio ON cio.orgCode = cca.orgCode " +
            "WHERE cca.isActive = 1" +
            "AND (:filter IS NULL OR LOWER(cca.areaCode) LIKE CONCAT('%', :filter, '%') " +
            "     OR LOWER(cca.areaName) LIKE CONCAT('%', :filter, '%') " +
            "     OR LOWER(ccat.areaTypeName) LIKE CONCAT('%', :filter, '%') " +
            "     OR LOWER(cio.orgName) LIKE CONCAT('%', :filter, '%') " +
            "     OR LOWER(ccw.wardName) LIKE CONCAT('%', :filter, '%')) " +
            "AND (:orgCode IS NULL OR :orgCode = '' OR cca.orgCode = :orgCode) " +
            "AND (:areaTypeCodes IS NULL OR ccat.areaTypeCode IN :areaTypeCodes) " +
            "ORDER BY cca.modifiedDate DESC")
    Page<ComInfAreaResponse> findAllCtgComAreas(
            @Param("filter") String filter,
            @Param("orgCode") String orgCode,
            @Param("areaTypeCodes") List<String> areaTypeCodes,
            Pageable pageable
    );




    @Query("select cif from ComInfArea cif where cif.orgCode in (:orgCodes)")
    List<ComInfArea> findAllByOrgCodes(@Param("orgCodes") List<String> orgCodes);

    ComInfArea  findByAreaCode(String areaCode);

    @Query("SELECT new com.naas.admin_service.features.area.dto.ComInfAreaDto(c.areaCode,c.areaName) " +
            "FROM ComInfArea c " +
            "WHERE c.areaCode=:areaCode")
    List<ComInfAreaDto>  findByAreaCodes(@Param("areaCode") String areaCode);



    @Query(value = "SELECT DISTINCT cw.WARD_CODE AS wardCode, cw.WARD_NAME AS wardName " +
            "FROM CTG_INF_WARD cw " +
            "JOIN CTG_INF_AREA ca ON cw.WARD_CODE = ca.WARD_CODE " +
            "WHERE (:orgCode IS NULL OR ca.ORG_CODE = :orgCode)", nativeQuery = true)
    List<CtgInfWardDto> getListWard(@Param("orgCode") String orgCode);


    @Query("SELECT new com.naas.admin_service.features.area.dto.ComInfAreaDto(c.areaCode,c.areaName,c.wardCode) " +
            "FROM ComInfArea c " )

    List<ComInfAreaDto> getListAreaByWard();

    @Query("SELECT new com.naas.admin_service.features.area.dto.ComInfAreaResponse(" +
            "cca.id, cca.areaCode, cca.areaName, cio.orgName, ccat.areaTypeName, ccw.wardName) " +
            "FROM ComInfArea cca " +
            "JOIN ComInfAreaType ccat ON cca.areaTypeCode = ccat.areaTypeCode " +
            "JOIN CtgInfWard ccw ON cca.wardCode = ccw.wardCode " +
            "JOIN ComInfOrganization cio ON cio.orgCode = cca.orgCode " +
            "WHERE cca.isActive = 1" +
            "AND (:orgCode IS NULL OR :orgCode = '' OR cca.orgCode = :orgCode) " +
            "AND (:areaTypeCodes IS NULL OR ccat.areaTypeCode IN :areaTypeCodes) " +
            "ORDER BY cca.modifiedDate DESC")
    List<ComInfAreaResponse> findAllCtgComAreasForExport(
            @Param("orgCode") String orgCode,
            @Param("areaTypeCodes") List<String> areaTypeCodes
    );
}
