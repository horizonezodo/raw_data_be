package ngvgroup.com.rpt.features.ctgcfgstattemplate.repository;

import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatTemplateSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgStatTemplateSheetRepository extends JpaRepository<CtgCfgStatTemplateSheet,Long> {
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

    List<CtgCfgStatTemplateSheet> findByTemplateCodeOrderBySheetDataAsc(String templateCode);

    List<CtgCfgStatTemplateSheet> findAllByTemplateCode(String templateCode);

    List<CtgCfgStatTemplateSheet> findAllByTemplateCodeAndOrgCode(String templateCode, String orgCode);

}
