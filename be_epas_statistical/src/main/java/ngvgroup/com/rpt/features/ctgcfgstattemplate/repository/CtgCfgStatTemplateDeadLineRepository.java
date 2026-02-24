package ngvgroup.com.rpt.features.ctgcfgstattemplate.repository;

import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatedeadline.CtgCfgStatTemplateDeadLineDTO;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatTemplateDeadLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgStatTemplateDeadLineRepository extends JpaRepository<CtgCfgStatTemplateDeadLine,Long> {
    @Query("""
        SELECT new ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatedeadline.CtgCfgStatTemplateDeadLineDTO(
            c.frequency,
            c.deadlineType,
            c.deadlineValue,
            c.deadlineDayTime
        )FROM CtgCfgStatTemplateDeadLine c
        WHERE c.isActive = 1
        AND c.templateCode =:templateCode
    """)
    CtgCfgStatTemplateDeadLineDTO getAllByTemplateCode(@Param("templateCode")String templateCode);

    void deleteAllByTemplateCode(String templateCode);

    Optional<CtgCfgStatTemplateDeadLine> findByTemplateCode(String templateCode);

    Optional<CtgCfgStatTemplateDeadLine> findCurrentByTemplateCode(String templateCode);

    @Query("""
    SELECT c.templateCode 
    FROM CtgCfgStatTemplateSheet c
    WHERE c.templateCode IN :templateCodes
      AND c.orgCode = :orgCode
      AND c.templateCode =:templateCode
      AND c.isActive = 1
""")
    List<String> findExistingTemplateCodesAndTemplateCode(@Param("templateCodes") List<String> templateCodes,
                                                          @Param("templateCode")String templateCode,
                                                          @Param("orgCode") String orgCode);

    List<CtgCfgStatTemplateDeadLine> findAllByTemplateCodeAndOrgCode(String templateCode,String orgCode);

}
