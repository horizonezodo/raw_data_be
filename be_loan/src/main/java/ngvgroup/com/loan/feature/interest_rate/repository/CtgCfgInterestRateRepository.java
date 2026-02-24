package ngvgroup.com.loan.feature.interest_rate.repository;

import ngvgroup.com.loan.feature.interest_rate.dto.CtgCfgInterestRateDto;
import ngvgroup.com.loan.feature.interest_rate.model.CtgCfgInterestRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgInterestRateRepository extends JpaRepository<CtgCfgInterestRate, Long> {

    @Query("SELECT new ngvgroup.com.loan.feature.interest_rate.dto.CtgCfgInterestRateDto(" +
            "c.interestCode,c.interestName," +
            "cc.commonName,c.currencyCode," +
            "c.moduleCode,co.orgName,'',c.isActive ,c.isByBalance) " +
            "FROM CtgCfgInterestRate c " +
            "LEFT JOIN CtgComCommon cc ON c.interestType=cc.commonCode " +
            "LEFT JOIN ComInfOrganization co ON co.orgCode=c.orgCode " +
            "WHERE (:keyword IS NULL OR :keyword ='' OR " +
            "LOWER(c.interestCode) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(c.interestName) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(cc.commonName) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(c.currencyCode) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(c.moduleCode) LIKE CONCAT('%', LOWER(:keyword), '%') OR "+
            "LOWER(co.orgName) LIKE CONCAT('%', LOWER(:keyword), '%') OR "+
            "LOWER('hiệu lực') LIKE LOWER(CONCAT('%', LOWER(:keyword), '%')) AND c.isActive=1 OR "+
            "LOWER('hết hiệu lực') LIKE LOWER(CONCAT('%', LOWER(:keyword), '%')) AND c.isActive=0) " +
            "AND (:orgCode IS NULL OR :orgCode = '' OR co.orgCode = :orgCode)" +
            "AND (:moduleCodes IS NULL OR c.moduleCode IN :moduleCodes) " +
            "ORDER BY c.modifiedDate DESC,co.orgName,c.moduleCode "
    )
    Page<CtgCfgInterestRateDto> searchAll(@Param("keyword") String keyword,@Param("orgCode") String orgCode,@Param("moduleCodes") List<String> moduleCodes, Pageable pageable);

    @Query("SELECT new ngvgroup.com.loan.feature.interest_rate.dto.CtgCfgInterestRateDto(" +
            "c.interestCode,c.interestName," +
            "cc.commonName,c.currencyCode," +
            "c.moduleCode,co.orgName,'',c.isActive,null ) " +
            "FROM CtgCfgInterestRate c " +
            "LEFT JOIN CtgComCommon cc ON c.interestType=cc.commonCode " +
            "LEFT JOIN ComInfOrganization co ON co.orgCode=c.orgCode " +
            "WHERE :keyword IS NULL OR :keyword ='' OR " +
            "LOWER(c.interestCode) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(c.interestName) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(cc.commonName) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(c.currencyCode) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(c.moduleCode) LIKE CONCAT('%', LOWER(:keyword), '%') OR "+
            "LOWER(co.orgName) LIKE CONCAT('%', LOWER(:keyword), '%') OR "+
            "LOWER('hiệu lực') LIKE LOWER(CONCAT('%', LOWER(:keyword), '%') ) AND c.isActive=1 OR "+
            "LOWER('hết hiệu lực') LIKE LOWER(CONCAT('%', LOWER(:keyword), '%') ) AND c.isActive=0 " +
            "ORDER BY co.orgName,c.moduleCode,c.modifiedDate DESC "
    )
    List<CtgCfgInterestRateDto> exportToExcel(@Param("keyword") String keyword);


    Optional<CtgCfgInterestRate> findCtgCfgInterestRateByInterestCode(@Param("interestCode") String interestCode);

    @Query("SELECT new ngvgroup.com.loan.feature.interest_rate.dto.CtgCfgInterestRateDto(" +
            "c.interestCode, c.interestName, " +
            "c.interestType, c.currencyCode, " +
            "c.moduleCode, c.orgCode, '',c.isActive ,c.isByBalance ) " +
            "FROM CtgCfgInterestRate c " +
            "WHERE c.interestCode=:interestCode")
    CtgCfgInterestRateDto getDetail(@Param("interestCode") String interestCode);


}
