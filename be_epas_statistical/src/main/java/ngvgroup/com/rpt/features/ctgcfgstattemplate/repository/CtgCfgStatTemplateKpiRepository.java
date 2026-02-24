package ngvgroup.com.rpt.features.ctgcfgstattemplate.repository;

import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatekpi.IndexKpiDto;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatTemplateKpi;
import ngvgroup.com.rpt.features.transactionreport.dto.DynamicTableRoutingDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgStatTemplateKpiRepository extends JpaRepository<CtgCfgStatTemplateKpi,Long> {
    @Query("""
    SELECT c.templateKpiCode 
    FROM CtgCfgStatTemplateKpi c
    WHERE c.templateKpiCode IN :templateKpiCodes
      AND c.orgCode = :orgCode
      AND c.templateCode = :templateCode
      AND c.isActive = 1
""")
    List<String> findExistingTemplateKpiCodesAndTemplateCode(@Param("templateKpiCodes") List<String> templateKpiCodes,
                                              @Param("templateCode")String templateCode,
                                              @Param("orgCode") String orgCode);
    List<CtgCfgStatTemplateKpi> findAllByTemplateKpiCodeInAndOrgCode(List<String> templateKpiCodes, String orgCode);

    List<CtgCfgStatTemplateKpi> findAllByTemplateCode(String templateCode);

    List<CtgCfgStatTemplateKpi> findAllByTemplateCodeAndOrgCode(String templateCode, String orgCode);

    @Modifying
    @Query("""
    DELETE FROM CtgCfgStatTemplateKpi e
    WHERE e.templateCode = :templateCode
      AND e.templateKpiCode IN :codes
      AND e.orgCode = :orgCode
""")
    void deleteAllByTemplateCodeAndTemplateKpiCodeInAndOrgCode(
            @Param("templateCode") String templateCode,
            @Param("codes") List<String> codes,
            @Param("orgCode") String orgCode);



    List<CtgCfgStatTemplateKpi> findByTemplateCodeOrderByRowAsc(String templateCode);

    @Query("""
    SELECT new ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatekpi.IndexKpiDto(
        t.templateCode,
        CONCAT(t.row,'_',t.column),
        t.kpiCode,
        t.templateKpiCode,
        t.templateKpiName
    )
    FROM CtgCfgStatTemplateKpi t
    WHERE (:templateCode IS NULL OR t.templateCode = :templateCode)
    AND CONCAT(t.row,'_',t.column) IN :indexs
        """)
    List<IndexKpiDto> getAllByTemplateCodes(List<String> indexs, String templateCode);

    @Query("""
        SELECT new ngvgroup.com.rpt.features.transactionreport.dto.DynamicTableRoutingDto(
            k.templateCode,
            k.templateKpiCode, 
            s.tableData
        )
        FROM CtgCfgStatTemplateKpi k
        JOIN CtgCfgStatTemplateSheet s
          ON k.areaId = s.areaId
          AND k.templateCode = s.templateCode
        WHERE k.templateCode IN :templateCodes
          AND k.templateKpiCode IN :templateKpiCodes
          AND s.tableData IS NOT NULL
        """)
    List<DynamicTableRoutingDto> findTableMapping(
            @Param("templateCodes") List<String> templateCodes,
            @Param("templateKpiCodes") List<String> templateKpiCodes
    );

    CtgCfgStatTemplateKpi findByTemplateKpiCode(String templateKpiCode);
}
