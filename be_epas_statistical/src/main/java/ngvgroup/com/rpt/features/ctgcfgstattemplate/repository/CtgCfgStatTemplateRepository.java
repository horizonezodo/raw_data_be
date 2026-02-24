package ngvgroup.com.rpt.features.ctgcfgstattemplate.repository;

import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplate.*;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgStatTemplateRepository extends JpaRepository<CtgCfgStatTemplate,Long> {

    @Query("""
        SELECT distinct c.circularName, c.circularCode
        FROM CtgCfgStatTemplate c
    """)
    List<Object[]> findAllCirculars();

    @Query("""
        SELECT new ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplate.CtgCfgStatTemplateDtoV2(
            c.templateGroupCode,
            c.templateGroupName,
            c.templateCode,
            c.templateName
        )
        FROM CtgCfgStatTemplate c
        WHERE c.circularCode = :circularCode
    """)
    List<CtgCfgStatTemplateDtoV2> findAllByCircularCode(@Param("circularCode") String circularCode);

    @Query("""
        SELECT new ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplate.ReportRuleDto(
            c.circularName,
            c.circularCode,
            c.templateGroupCode,
            c.templateGroupName,
            c.templateCode,
            c.templateName,
            cc.commonName,
            c.regulatoryTypeName,
            cs.statusName,
            c.description
        )
        FROM CtgCfgStatTemplate c
        LEFT JOIN ComCfgCommon cc ON cc.commonCode = c.frequency
        LEFT JOIN CtgCfgWorkflow cw ON cw.workflowCode = :regulatoryTypeCode
        LEFT JOIN CtgCfgStatus cs ON cw.initialStatusCode = cs.statusCode
        WHERE (:regulatoryTypeCode IS NULL OR c.regulatoryTypeCode = :regulatoryTypeCode)
        AND (:commonCode IS NULL OR c.frequency = :commonCode)
        AND (:templateGroupCodes IS NULL OR c.templateGroupCode IN :templateGroupCodes)
        AND (:circularCodes IS NULL OR c.circularCode in :circularCodes)
        AND (:defaultCircularCodes IS NULL OR c.circularCode in :defaultCircularCodes)
        AND (:search IS NULL OR
               LOWER(c.templateCode) LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(cc.commonName) LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(c.templateName) LIKE LOWER(CONCAT('%', :search, '%')))
               
    """)
    Page<ReportRuleDto> getListReportRule(
            @Param("regulatoryTypeCode") String regulatoryTypeCode,
            @Param("commonCode") String commonCode,//định kỳ
            @Param("templateGroupCodes") List<String> templateGroupCodes,
            @Param("circularCodes") List<String> circularCodes,
            @Param("defaultCircularCodes") List<String> defaultCircularCodes,
            @Param("search") String search,
            Pageable pageable
    );

    @Query("""
        SELECT new ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplate.CtgCfgStatTemplateDtoV3(
            c.id,
            c.orgCode,
            c.templateCode,
            c.templateName,
            c.templateGroupCode,
            c.effectiveDate,
            c.expiryDate,
            c.columnStart,
            c.frequency,
            c.expressionSql,
            c.templateDataType,
            c.generateDataType,
            c.unitKpi,
            c.dataFormatType,
            c.decimalScale,
            c.decimalFormat,
            c.roundingDigits,
            c.separator,
            c.decimalSeparator,
            c.zeroDisplayFormat,
            c.negativeDisplayFormat,
            c.description,
            c.templateFileName,
            c.templateReportFileName,
            c.userGuideFileName,
            c.circularName,
            c.circularCode,
            c.regulatoryTypeCode,
            c.regulatoryTypeName,
            co.commonName
        )
        FROM CtgCfgStatTemplate c
        LEFT JOIN ComCfgCommon co
        ON c.frequency = co.commonCode
        WHERE c.isActive = 1
        AND (:circularCodes IS NULL OR c.circularCode IN :circularCodes)
        AND (
            :keyword IS NULL OR
            LOWER(c.templateCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(c.templateName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR 
            LOWER(c.templateGroupCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        order by c.modifiedDate desc
    """)
    List<CtgCfgStatTemplateDtoV3> listTemplateByCirculars(
            @Param("keyword") String keyword,
            @Param("circularCodes") List<String> circularCodes
    );

    @Query("""
        SELECT new ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplate.ExportExcelData(
            c.templateCode,
            c.templateName,
            c.templateGroupCode,
            c.description,
            c.frequency
        )
        FROM CtgCfgStatTemplate c 
        WHERE c.isActive = 1
        AND (:groupCodes IS NULL OR c.templateGroupCode in (:groupCodes))
        AND (:circularCodes IS NULL OR c.circularCode in (:circularCodes))
    """)
    List<ExportExcelData> exportExcelData(@Param("groupCodes")List<String> groupCodes,@Param("circularCodes")List<String> circularCodes);

    @Query("""
        SELECT new ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplate.CtgCfgStatTemplateDtoV3(
            c.id,
            c.orgCode,
            c.templateCode,
            c.templateName,
            c.templateGroupCode,
            c.effectiveDate,
            c.expiryDate,
            c.columnStart,
            c.frequency,
            c.expressionSql,
            c.templateDataType,
            c.generateDataType,
            c.unitKpi,
            c.dataFormatType,
            c.decimalScale,
            c.decimalFormat,
            c.roundingDigits,
            c.separator,
            c.decimalSeparator,
            c.zeroDisplayFormat,
            c.negativeDisplayFormat,
            c.description,
            c.templateFileName,
            c.templateReportFileName,
            c.userGuideFileName,
            c.circularName,
            c.circularCode,
            c.regulatoryTypeCode,
            c.regulatoryTypeName,
            co.commonName
        )
    FROM CtgCfgStatTemplate c
    LEFT JOIN ComCfgCommon co ON c.frequency = co.commonCode
    WHERE c.isActive = 1
      AND (:templateGroupCodes IS NULL OR c.templateGroupCode IN :templateGroupCodes)
      AND (:circularCode IS NULL OR c.circularCode IN :circularCode)
      AND (:keyword IS NULL OR :keyword = '' OR
           LOWER(c.templateCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
           LOWER(c.templateName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
           LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
           LOWER(c.templateGroupCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
    ORDER BY c.modifiedDate DESC
""")
    Page<CtgCfgStatTemplateDtoV3> pageTemplate(
            @Param("keyword") String keyword,
            @Param("templateGroupCodes") List<String> templateGroupCodes,
            @Param("circularCode") List<String> circularCode,
            Pageable pageable
    );

    @Query("""
        Select distinct new ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplate.CtgCfgStatTemplateDtoV1(
            c.statTypeCode,
            c.statTypeName
        ) 
        from CtgCfgStatType c 
        where c.isActive = 1 and (:keyword IS NULL OR
        LOWER(c.statTypeCode) LIKE LOWER(CONCAT('%', :keyword, '%')))
    """)
    Page<CtgCfgStatTemplateDtoV1> pageTemplateGroupCode(
            @Param("keyword")String keyword,
            Pageable pageable
    );

    boolean existsByTemplateCodeAndOrgCode(String templateCode, String orgCode);

    CtgCfgStatTemplate findByTemplateCodeAndOrgCode(String templateCode,String orgCode);

    @Modifying
    @Query("""
        DELETE FROM CtgCfgStatTemplate e
        WHERE e.templateCode = :templateCode
    """)
    void deleteTemplateByTemplateCode(@Param("templateCode")String templateCode);

    CtgCfgStatTemplate findByTemplateCode(String templateCode);

    List<CtgCfgStatTemplate> findByRegulatoryTypeCodeAndFrequency(String regulatoryTypeCode, String frequency);

    @Query("""
        SELECT DISTINCT new ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplate.CtgCfgStatTemplateDtoV1(
            c.regulatoryTypeCode,
            c.regulatoryTypeName
        )
        FROM CtgCfgStatTemplate c
        WHERE c.isActive = 1
    """)
    List<CtgCfgStatTemplateDtoV1> getAllDistinct();

    CtgCfgStatTemplate findFirstByTemplateCodeIn(List<String> codes);

    @Query("""
        SELECT distinct c.templateGroupName, c.templateGroupCode
        FROM CtgCfgStatTemplate c
    """)
    List<Object[]> findAllTemplateGroupCode();

    @Query("""
        SELECT new ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplate.CtgCfgStatTemplateDtoV2(
            c.templateGroupCode,
            c.templateGroupName,
            c.templateCode,
            c.templateName
        )
        FROM CtgCfgStatTemplate c
        WHERE c.templateGroupCode = :templateGroupCode
    """)
    List<CtgCfgStatTemplateDtoV2> findAllByTemplateGroupCode(String templateGroupCode);

    @Query("""
        SELECT c.expressionSql
        FROM CtgCfgStatTemplate c
        WHERE c.templateCode = :templateCode
    """)
    String findExpressionSqlByTemplateCode(@Param("templateCode") String templateCode);
}
