package ngvgroup.com.loan.feature.loan_purpose.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ngvgroup.com.loan.feature.loan_purpose.dto.CtgCfgLoanPurposeResponse;
import ngvgroup.com.loan.feature.loan_purpose.model.CtgCfgLoanPurpose;

@Repository
public interface CtgCfgLoanPurposeRepository extends JpaRepository<CtgCfgLoanPurpose, Long> {
    @Query("select new ngvgroup.com.loan.feature.loan_purpose.dto.CtgCfgLoanPurposeResponse(" +
            "cclp.id, cclp.purposeCode, cclp.purposeName, cclp.loanLimitMin, cclp.loanLimitMax, c.commonName, " +
            "cclp.isActive) " +
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
            "(cclp.isActive = 1 and 'hiệu lực' ilike %:filter%) or " +
            "(cclp.isActive = 0 and 'hết hiệu lực' ilike %:filter%)" +
            ")")
    Page<CtgCfgLoanPurposeResponse> searchCtgCfgLoanPurpose(@Param("filter") String filter, Pageable pageable);

    @Query("select new ngvgroup.com.loan.feature.loan_purpose.dto.CtgCfgLoanPurposeResponse(cclp.id, cclp.purposeCode, cclp.purposeName, cclp.loanLimitMin, cclp.loanLimitMax, c.commonName, " +
            "cclp.isActive) from CtgCfgLoanPurpose cclp left join CtgComCommon c on c.commonCode=cclp.riskLevel where " +
            "cclp.isDelete = 0 and (:filter is null or lower(cclp.purposeCode) ilike %:filter% or lower(cclp.purposeName) ilike %:filter% " +
            "or to_char(cclp.loanLimitMin) ilike %:filter% or to_char(cclp.loanLimitMax) ilike %:filter% " +
            "or lower(cclp.riskLevel) ilike %:filter% or (cclp.isActive = 1 and 'hiệu lực' ilike %:filter%) or (cclp.isActive = 0 and 'hết hiệu lực' ilike %:filter%))")
    List<CtgCfgLoanPurposeResponse> exportToExcel(@Param("filter") String filter);

    List<CtgCfgLoanPurpose> findCtgCfgLoanPurposesByPurposeCodeAndIsDelete(String purposeCode, int isDelete);


    Optional<CtgCfgLoanPurpose> findCtgCfgLoanPurposeByPurposeCode(String purposeCode);
}

