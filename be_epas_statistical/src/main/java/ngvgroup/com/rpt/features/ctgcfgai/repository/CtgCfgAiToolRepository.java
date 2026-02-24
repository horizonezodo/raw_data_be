package ngvgroup.com.rpt.features.ctgcfgai.repository;

import ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitool.CtgCfgAiToolDTOV1;
import ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitool.CtgCfgAiToolDTOV2;
import ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitool.ExportExcelData;
import ngvgroup.com.rpt.features.ctgcfgai.model.CtgCfgAiTool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgAiToolRepository extends JpaRepository<CtgCfgAiTool, Long> {
    CtgCfgAiTool findByToolAiCode(String toolAiCode);

    @Query("""
                SELECT new ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitool.CtgCfgAiToolDTOV1(
                    c.toolAiCode,
                    c.toolAiName,
                    c.toolAiTypeCode,
                    ct.toolAiTypeName,
                    c.description
                )FROM CtgCfgAiTool c
                LEFT JOIN CtgCfgAiToolType ct 
                ON c.toolAiTypeCode = ct.toolAiTypeCode
                where
                :keyword IS NULL OR
                LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(ct.toolAiTypeName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(c.toolAiTypeCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                ORDER by c.modifiedDate desc
            """)
    Page<CtgCfgAiToolDTOV1> pageToolAI(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
                SELECT new ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitool.ExportExcelData(
                    c.toolAiCode,
                    c.toolAiName,
                    c.toolAiTypeCode,
                    ct.toolAiTypeName,
                    c.description
                )FROM CtgCfgAiTool c
                LEFT JOIN CtgCfgAiToolType ct 
                ON c.toolAiTypeCode = ct.toolAiTypeCode
                where
                :keyword IS NULL OR
                LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(ct.toolAiTypeName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(c.toolAiTypeCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<ExportExcelData> listToolAI(@Param("keyword") String keyword);

    @Query("""
                SELECT new ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitool.CtgCfgAiToolDTOV2(
                    c.toolAiCode,
                    c.toolAiName,
                    c.toolAiTypeCode,
                    ct.toolAiTypeName,
                    c.inputParameter,
                    c.templateConfig,
                    c.outputFormat,
                    c.timeoutSecond,
                    c.retryCount,
                    c.requiresAuth,
                    c.allowedRole,
                    c.description
                )FROM CtgCfgAiTool c
                LEFT JOIN CtgCfgAiToolType ct
                ON c.toolAiTypeCode = ct.toolAiTypeCode
                ORDER by c.modifiedDate desc
            """)
    List<CtgCfgAiToolDTOV2> getAllTools();

    boolean existsByToolAiCode(String toolAiCode);
}
