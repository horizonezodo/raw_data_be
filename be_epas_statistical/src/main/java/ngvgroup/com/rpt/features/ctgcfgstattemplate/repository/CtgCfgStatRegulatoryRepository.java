package ngvgroup.com.rpt.features.ctgcfgstattemplate.repository;

import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.StatFilterTreeItem;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.StatRegulatoryInfo;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.CtgCfgStatRegulatoryResponse;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatRegulatory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgStatRegulatoryRepository extends JpaRepository<CtgCfgStatRegulatory, Long>, JpaSpecificationExecutor<CtgCfgStatRegulatory> {
    Optional<CtgCfgStatRegulatory> findByStatRegulatoryCode(String statRegulatoryCode);
    boolean existsByStatRegulatoryCode(String statRegulatoryCode);

    @Query("""
                select distinct new ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.StatFilterTreeItem(
                    r.reportModuleCode,
                    c.commonCode,
                    c.commonName,
                    s.statTypeCode,
                    s.statTypeName)
                from CtgCfgStatRegulatory r
                left join ComCfgCommon c on r.reportModuleCode = c.commonCode
                left join CtgCfgStatType s on s.statTypeCode = r.statTypeCode
                ORDER BY c.commonName, s.statTypeName
            """)
    List<StatFilterTreeItem> getFilterTreeRaw();

    @Query("""
                    select new ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.CtgCfgStatRegulatoryResponse(
                    r.statRegulatoryCode,
                    r.statRegulatoryName,
                    r.reportModuleCode,
                    c.commonName,
                    r.statTypeCode,
                    s.statTypeName,
                    r.isActive,
                    r.recordStatus,
                    r.sortNumber,
                    r.description
                    )
                    from CtgCfgStatRegulatory r
                    left join ComCfgCommon c on r.reportModuleCode = c.commonCode
                    left join CtgCfgStatType s on s.statTypeCode = r.statTypeCode
                    where :statRegulatoryCode = r.statRegulatoryCode
            """)
    CtgCfgStatRegulatoryResponse findDetail(@Param("statRegulatoryCode") String statRegulatoryCode);

    @Query("""
                    select new ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.StatRegulatoryInfo(r.statRegulatoryCode, r.statRegulatoryName)
                    from CtgCfgStatRegulatory r
                    where (r.statTypeCode = :statTypeCodes) and
                                (:keyword is null or
                            lower(r.statRegulatoryCode) like concat('%', :keyword, '%') or
                            lower(r.statRegulatoryName) like concat('%', :keyword, '%'))
            """)
    List<StatRegulatoryInfo> getStatRegulatoryByStatTypeCode(@Param("statTypeCodes") String statTypeCodes, @Param("keyword") String keyword);

}
