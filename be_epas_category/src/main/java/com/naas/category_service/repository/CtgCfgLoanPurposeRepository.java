package com.naas.category_service.repository;

import com.naas.category_service.dto.CtgCfgLoanPurpose.CtgCfgLoanPurposeResponse;
import com.naas.category_service.model.CtgCfgLoanPurpose;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgLoanPurposeRepository extends JpaRepository<CtgCfgLoanPurpose, Long> {
    @Query("select new com.naas.category_service.dto.CtgCfgLoanPurpose.CtgCfgLoanPurposeResponse(" +
            "cclp.id, cclp.purposeCode, cclp.purposeName, cclp.loanLimitMin, cclp.loanLimitMax, c.commonName, " +
            "null, cclp.isActive) " +
            "from CtgCfgLoanPurpose cclp " +
            "left join CtgComCommon c on c.commonCode = cclp.riskLevel " +
            "where cclp.isDelete = 0 and (" +
            ":filter is null or " +
            "lower(cclp.purposeCode) ilike %:filter% or " +
            "lower(cclp.purposeName) ilike %:filter% or " +
            "to_char(cclp.loanLimitMin) ilike %:filter% or " +
            "to_char(cclp.loanLimitMax) ilike %:filter% or " +
            "lower(cclp.riskLevel) ilike %:filter% or " +
            "lower(c.commonName) ilike %:filter% or " +
            "(cclp.isActive = true and 'hiệu lực' ilike %:filter%) or " +
            "(cclp.isActive = false and 'hết hiệu lực' ilike %:filter%)" +
            ")")
    Page<CtgCfgLoanPurposeResponse> searchCtgCfgLoanPurpose(@Param("filter") String filter, Pageable pageable);

    @Query("select new com.naas.category_service.dto.CtgCfgLoanPurpose.CtgCfgLoanPurposeResponse(cclp.id, cclp.purposeCode, cclp.purposeName, cclp.loanLimitMin, cclp.loanLimitMax, c.commonName, " +
            "null, cclp.isActive) from CtgCfgLoanPurpose cclp left join CtgComCommon c on c.commonCode=cclp.riskLevel where " +
            "cclp.isDelete = 0 and (:filter is null or lower(cclp.purposeCode) ilike %:filter% or lower(cclp.purposeName) ilike %:filter% " +
            "or to_char(cclp.loanLimitMin) ilike %:filter% or to_char(cclp.loanLimitMax) ilike %:filter% " +
            "or lower(cclp.riskLevel) ilike %:filter% or (cclp.isActive = true and 'hiệu lực' ilike %:filter%) or (cclp.isActive = false and 'hết hiệu lực' ilike %:filter%))")
    List<CtgCfgLoanPurposeResponse> exportToExcel(@Param("filter") String filter);

    List<CtgCfgLoanPurpose> findCtgCfgLoanPurposesByPurposeCodeAndIsDelete(String purposeCode, int isDelete);


    Optional<CtgCfgLoanPurpose> findCtgCfgLoanPurposeByPurposeCode(String purposeCode);

    @Modifying
    @Transactional
    @Query("UPDATE CtgCfgLoanPurpose c SET c.purposeName=:purposeName,c.orgCode=:orgCode,c.loanLimitMin=:loanLimitMin," +
            "c.loanLimitMax=:loanLimitMax,c.riskLevel=:riskLevel,c.isActive=:isActive,c.description=:description,c.modifiedDate=CURRENT_TIMESTAMP " +
            "WHERE c.purposeCode=:purposeCode")
    void updateCtgCfgLoanPurpose(@Param("purposeCode") String purposeCode,
                                 @Param("purposeName") String purposeName,
                                 @Param("orgCode") String orgCode,
                                 @Param("loanLimitMin") BigDecimal loanLimitMin,
                                 @Param("loanLimitMax") BigDecimal loanLimitMax,
                                 @Param("riskLevel") String riskLevel,
                                 @Param("isActive") Boolean isActive,
                                 @Param("description") String description);
}
