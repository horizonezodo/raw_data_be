package ngvgroup.com.rpt.features.ctgcfgstattemplate.repository;

import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatorytype.CtgCfgStatRegulatoryTypeDTO;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatRegulatoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgStatRegulatoryTypeRepository extends JpaRepository<CtgCfgStatRegulatoryType, Long> {

    @Query("""
        SELECT new ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatorytype.CtgCfgStatRegulatoryTypeDTO(
            c.regulatoryTypeCode,
            c.regulatoryTypeName
        )FROM CtgCfgStatRegulatoryType c
        where c.isActive = 1
    """)
    List<CtgCfgStatRegulatoryTypeDTO> getAllData();

    Optional<CtgCfgStatRegulatoryType> findByRegulatoryTypeCode(String code);

    @Query("""
    SELECT new ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatorytype.CtgCfgStatRegulatoryTypeDTO(
        c.id,
        c.regulatoryTypeCode,
        c.regulatoryTypeName,
        c.issuedBy,
        c.issuedDate,
        c.effectiveDate,
        c.description,
        cc.workflowCode
    )
    FROM CtgCfgStatRegulatoryType c
    LEFT JOIN CtgCfgStatRegulatoryWf cc
        ON c.regulatoryTypeCode = cc.regulatoryTypeCode
    WHERE c.isActive = 1
      AND (:keyword IS NULL OR
           LOWER(c.regulatoryTypeCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
           LOWER(c.regulatoryTypeName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
           LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
    ORDER BY c.modifiedDate desc      
""")
    Page<CtgCfgStatRegulatoryTypeDTO> pageData(@Param("keyword")String keyword, Pageable pageable);

    @Query("""
    SELECT new ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatorytype.CtgCfgStatRegulatoryTypeDTO(
        c.id,
        c.regulatoryTypeCode,
        c.regulatoryTypeName,
        c.issuedBy,
        c.issuedDate,
        c.effectiveDate,
        c.description,
        cc.workflowCode
    )
    FROM CtgCfgStatRegulatoryType c
    LEFT JOIN CtgCfgStatRegulatoryWf cc
        ON c.regulatoryTypeCode = cc.regulatoryTypeCode
    WHERE c.isActive = 1
      AND (:keyword IS NULL OR
           LOWER(c.regulatoryTypeCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
           LOWER(c.regulatoryTypeName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
           LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
""")
    List<CtgCfgStatRegulatoryTypeDTO> exportExcelData(@Param("keyword")String keyword);

    @Query("""
SELECT new ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatorytype.CtgCfgStatRegulatoryTypeDTO(
            c.id,
            c.regulatoryTypeCode,
            c.regulatoryTypeName,
            c.issuedBy,
            c.issuedDate,
            c.effectiveDate,
            c.description,
            cc.workflowCode
        )FROM CtgCfgStatRegulatoryType c
        LEFT JOIN CtgCfgStatRegulatoryWf  cc
        ON c.regulatoryTypeCode = cc.regulatoryTypeCode
        where c.isActive = 1
        AND c.id =:id
""")
    CtgCfgStatRegulatoryTypeDTO getDetailById(@Param("id")Long id);
}
