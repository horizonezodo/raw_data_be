package ngvgroup.com.rpt.features.ctgcfgstatkpi.repository;

import ngvgroup.com.rpt.features.ctgcfgstatkpi.dto.CtgCfgStatTypeKpiDto;
import ngvgroup.com.rpt.features.ctgcfgstatkpi.model.CtgCfgStatTypeKpi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgStatTypeKpiRepository extends JpaRepository<CtgCfgStatTypeKpi,Long> {

    @Query("""
        SELECT NEW ngvgroup.com.rpt.features.ctgcfgstatkpi.dto.CtgCfgStatTypeKpiDto(
            c.kpiTypeCode,
            c.kpiTypeName,
            c.description
        )FROM CtgCfgStatTypeKpi c
        WHERE c.isActive = 1
        AND (
        :keyword IS NULL OR
        LOWER(c.kpiTypeCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(c.kpiTypeName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        order by c.modifiedDate desc
    """)
    Page<CtgCfgStatTypeKpiDto> pageTypeKpi(@Param("keyword")String keyword, Pageable pageable);

    @Query("""
        SELECT NEW ngvgroup.com.rpt.features.ctgcfgstatkpi.dto.CtgCfgStatTypeKpiDto(
            c.kpiTypeCode,
            c.kpiTypeName,
            c.description
        )FROM CtgCfgStatTypeKpi c
        WHERE c.isActive = 1
        AND (
        :keyword IS NULL OR
        LOWER(c.kpiTypeCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(c.kpiTypeName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """)
    List<CtgCfgStatTypeKpiDto> listTypeKpi(@Param("keyword")String keyword);

    CtgCfgStatTypeKpi findByKpiTypeCode(String kpiTypeCode);

    boolean existsByKpiTypeCode(String kpiTypeCode);
}
