package ngvgroup.com.rpt.features.ctgcfgstatkpi.repository;

import ngvgroup.com.rpt.features.ctgcfgstatkpi.dto.CtgCfgStatKpiDto;
import ngvgroup.com.rpt.features.ctgcfgstatkpi.model.CtgCfgStatKpi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgStatKpiRepository extends JpaRepository<CtgCfgStatKpi,Long> {

    @Query("""
    SELECT new ngvgroup.com.rpt.features.ctgcfgstatkpi.dto.CtgCfgStatKpiDto(
        c.kpiCode,
        c.kpiName,
        c.description,
        c.id
    )
    FROM CtgCfgStatKpi c
    LEFT JOIN CtgCfgStatTypeKpi ct ON c.kpiTypeCode = ct.kpiTypeCode
    WHERE 
        (
            :keyword IS NULL 
            OR LOWER(c.kpiCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(c.kpiName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        AND (:kpiTypeCode IS NULL OR c.kpiTypeCode IN :kpiTypeCode)
    ORDER BY c.modifiedDate DESC
""")

    Page<CtgCfgStatKpiDto> searchAllStatKpi(@Param("keyword") String keyword, @Param("kpiTypeCode") List<String> kpiTypeCode, Pageable pageable);


    @Query("SELECT new ngvgroup.com.rpt.features.ctgcfgstatkpi.dto.CtgCfgStatKpiDto(" +
            "c.kpiCode,c.kpiName,c.description,c.id) " +
            "FROM CtgCfgStatKpi c " +
            "LEFT JOIN CtgCfgStatTypeKpi ct ON c.kpiTypeCode=ct.kpiTypeCode " +
            "WHERE " +
            "(:kpiTypeCode IS NULL OR c.kpiTypeCode IN :kpiTypeCode) " +
            "ORDER BY c.modifiedDate DESC " )
    List<CtgCfgStatKpiDto> exportToExcel(@Param("kpiTypeCode") List<String> kpiTypeCode);

    Optional<CtgCfgStatKpi> findByKpiCode(@Param("kpiCode")String kpiCode);

    CtgCfgStatKpi getByKpiCode(String kpiCode);

    List<CtgCfgStatKpi> getAllByParentCode(String parentCode);

    @Query("SELECT new ngvgroup.com.rpt.features.ctgcfgstatkpi.dto.CtgCfgStatKpiDto(" +
            "c.kpiCode,c.kpiName) " +
            "FROM CtgCfgStatKpi c " +
            "JOIN CtgCfgStatTypeKpi ct ON c.kpiTypeCode=ct.kpiTypeCode " +
            "WHERE :keyword IS NULL OR " +
            "LOWER(c.kpiCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.kpiName) LIKE LOWER(CONCAT('%', :keyword, '%')) ")
    Page<CtgCfgStatKpiDto> findAllKpiData(@Param("keyword") String keyword, Pageable pageable);




}
