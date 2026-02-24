package ngvgroup.com.rpt.features.ctgcfgai.repository;

import ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitooltype.CtgCfgAiToolTypeDTO;
import ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitooltype.ListAIToolTypeExcel;
import ngvgroup.com.rpt.features.ctgcfgai.model.CtgCfgAiToolType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgAiToolTypeRepository extends JpaRepository<CtgCfgAiToolType,Long> {
    CtgCfgAiToolType findByToolAiTypeCode(String toolAiTypeCode);
    List<CtgCfgAiToolType> findByIsActiveTrue();

    @Query("""
        SELECT new ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitooltype.CtgCfgAiToolTypeDTO(
            c.toolAiTypeCode,
            c.toolAiTypeName,
            c.description
        )FROM CtgCfgAiToolType c
        WHERE
        :keyword IS NULL OR
        LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(c.toolAiTypeName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(c.toolAiTypeCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
        order by c.modifiedDate desc
    """)
    Page<CtgCfgAiToolTypeDTO> getPageAITool(@Param("keyword")String keyword, Pageable pageable);

    @Query("""
        SELECT new ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitooltype.ListAIToolTypeExcel(
            c.toolAiTypeCode,
            c.toolAiTypeName,
            c.description
        )FROM CtgCfgAiToolType c
        WHERE
        :keyword IS NULL OR
        LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(c.toolAiTypeName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(c.toolAiTypeCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<ListAIToolTypeExcel> listApiTool(@Param("keyword")String keyword);

    boolean existsByToolAiTypeCode(String toolAiTypeCode);
}
