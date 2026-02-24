package ngvgroup.com.rpt.features.ctgcfgstattemplate.repository;

import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.StatFilterTreeItem;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.CtgCfgStatTypeResponse;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgStatTypeRepository extends JpaRepository<CtgCfgStatType, Long> {
        CtgCfgStatType findByStatTypeCode(String statTypeCode);
        boolean existsByStatTypeCode(String statTypeCode);

        @Query("""
                            select new ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.CtgCfgStatTypeResponse(t.statTypeCode, t.statTypeName, t.reportModuleCode)
                            from CtgCfgStatType t
                            where t.reportModuleCode = :commonCode
                        """)
        List<CtgCfgStatTypeResponse> getStatTypeByReportModuleCode(@Param("commonCode") String commonCode);

        @Query("""
                        select new ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.CtgCfgStatTypeResponse(t.statTypeCode, t.statTypeName, t.reportModuleCode)
                        from CtgCfgStatType t
                        left join ComCfgCommon c on t.reportModuleCode = c.commonCode
                        where (:keyword is null or
                                        lower(t.statTypeName) like concat('%', lower(:keyword), '%') or
                                        lower(t.statTypeCode) like concat('%', lower(:keyword), '%') or
                                        lower(t.reportModuleCode) like concat('%', lower(:keyword), '%') or
                                        lower(c.commonName) like concat('%', lower(:keyword), '%'))
                        """)
        List<CtgCfgStatTypeResponse> getAllStatType(@Param("keyword") String keyword);

        @Query("""
                            select distinct new ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.StatFilterTreeItem(
                                    t.reportModuleCode,
                                    c.commonCode,
                                    c.commonName,
                                    t.statTypeCode,
                                    t.statTypeName
                                )
                                from CtgCfgStatType t
                                left join ComCfgCommon c on t.reportModuleCode = c.commonCode
                                group by t.reportModuleCode, t.statTypeCode, t.statTypeName, c.commonCode, c.commonName
                        """)
        List<StatFilterTreeItem> getFilterTree();

        @Query("""
                        SELECT t
                        FROM CtgCfgStatType t
                        LEFT JOIN ComCfgCommon c ON t.reportModuleCode = c.commonCode
                        WHERE (
                                :keyword IS NULL OR
                                lower(t.statTypeCode) LIKE CONCAT('%', lower(:keyword), '%') OR
                                lower(t.statTypeName) LIKE CONCAT('%', lower(:keyword), '%') OR
                                lower(t.reportModuleCode) LIKE CONCAT('%', lower(:keyword), '%') OR
                                lower(c.commonName) LIKE CONCAT('%', lower(:keyword), '%')
                              )
                          AND (:reportModuleCodes IS NULL OR t.reportModuleCode IN :reportModuleCodes)
                        ORDER BY t.modifiedDate DESC
                        """)
        Page<CtgCfgStatType> search(@Param("keyword") String keyword,
                        @Param("reportModuleCodes") List<String> reportModuleCodes,
                        Pageable pageable);
}
