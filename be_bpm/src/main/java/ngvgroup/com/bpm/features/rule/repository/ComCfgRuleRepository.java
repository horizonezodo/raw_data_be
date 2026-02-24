package ngvgroup.com.bpm.features.rule.repository;

import ngvgroup.com.bpm.features.rule.model.ComCfgRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComCfgRuleRepository extends JpaRepository<ComCfgRule, Long> {

    @Query("SELECT r.ruleCode FROM ComCfgRule r WHERE r.orgCode = :orgCode AND r.parentCode = :parentCode")
    Optional<String> findRuleCode(String orgCode, String parentCode);

    ComCfgRule findByRuleCodeAndIsDelete(String ruleCode, int status);

    boolean existsByRuleCodeAndIsDelete(String ruleCode, int status);

    @Query("SELECT r FROM ComCfgRule r " +
            "WHERE r.isDelete = 0 AND (:keyword IS NULL OR " +
            "LOWER(r.ruleCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.ruleName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.parentCode) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<ComCfgRule> searchRulesByName(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT r FROM ComCfgRule r " +
            "WHERE r.isDelete = 0 AND (:ruleCode IS NULL OR r.ruleCode = :ruleCode) AND " +
            "(:ruleName IS NULL OR r.ruleName = :ruleName) AND " +
            "(:parentCode IS NULL OR r.parentCode = :parentCode) AND " +
            "(:orgCode IS NULL OR r.orgCode = :orgCode)")
    Page<ComCfgRule> searchToExportExcel(@Param("ruleCode") String ruleCode, @Param("ruleName") String ruleName,
                                         @Param("parentCode") String parentCode, @Param("orgCode") String orgCode, Pageable pageable);

    @Query("SELECT r FROM ComCfgRule r " +
            "WHERE r.isDelete = 0 AND (:ruleCode IS NULL OR r.ruleCode = :ruleCode) AND " +
            "(:ruleName IS NULL OR r.ruleName = :ruleName) AND " +
            "(:parentCode IS NULL OR r.parentCode = :parentCode) AND " +
            "(:orgCode IS NULL OR r.orgCode = :orgCode)")
    Page<ComCfgRule> search(@Param("ruleCode") String ruleCode, @Param("ruleName") String ruleName,
                            @Param("parentCode") String parentCode, @Param("orgCode") String orgCode, Pageable pageable);

    @Query("Select DISTINCT ccru.userId from ComCfgRule ccr " +
            "INNER JOIN ComCfgRuleUser ccru ON ccr.ruleCode = ccru.ruleCode " +
            "WHERE ccr.parentCode IN :parentCode AND ccr.orgCode = :orgCode")
    List<String> findAllByParentCodeAndOrgCode(@Param("parentCode") List<String> parentCodes, @Param("orgCode") String orgCode);

    ComCfgRule findByRuleCodeAndOrgCodeAndIsDelete(String ruleCode,String orgCode,int isDelete);


    @Query("SELECT r FROM ComCfgRule r " +
            "WHERE r.isDelete = 0 " +
            "  AND (:ruleCode IS NULL OR r.ruleCode = :ruleCode) " +
            "  AND (:ruleName IS NULL OR r.ruleName = :ruleName) " +
            "  AND (:parentCode IS NULL OR r.parentCode = :parentCode) " +
            "  AND (:orgCode IS NULL OR r.orgCode = :orgCode) " +
            "  AND (:keyword IS NULL OR (" +
            "         r.ruleCode LIKE CONCAT('%', :keyword, '%') " +
            "      OR r.ruleName LIKE CONCAT('%', :keyword, '%') " +
            "      OR r.parentCode LIKE CONCAT('%', :keyword, '%') " +
            "      OR r.orgCode LIKE CONCAT('%', :keyword, '%')" +
            "  ))")
    Page<ComCfgRule> searchAll(@Param("ruleCode") String ruleCode,
                               @Param("ruleName") String ruleName,
                               @Param("parentCode") String parentCode,
                               @Param("orgCode") String orgCode,
                               @Param("keyword") String keyword,
                               Pageable pageable);

    @Query("Select r from ComCfgRule r where r.isDelete = 0 and r.ruleCode in :ruleCodes")
    List<ComCfgRule> findAllRule(@Param("ruleCodes")List<String> ruleCodes);

}
