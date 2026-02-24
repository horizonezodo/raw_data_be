package com.naas.category_service.repository;

import com.naas.category_service.dto.CtgCfgInterestRateDtl.CtgCfgInterestRateDtlDto;
import com.naas.category_service.model.CtgCfgInterestRateDtl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CtgCfgInterestRateDtlRepository extends JpaRepository<CtgCfgInterestRateDtl, Long> {



    List<CtgCfgInterestRateDtl> findCtgCfgInterestRateDtlsByInterestCode(String interestCode);


    @Query(value = "SELECT new com.naas.category_service.dto.CtgCfgInterestRateDtl.CtgCfgInterestRateDtlDto(" +
            "c.id," +
            "c.interestRate, c.negotiatedRate, " +
            "c.previousRate, c.effectiveDate, " +
            "c.amountFrom, c.amountTo, " +
            "c.docNo, c.docEffectiveDate) " +
            "FROM CtgCfgInterestRateDtl c " +
            "WHERE c.interestCode = :interestCode " +
            "AND (:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(STR(c.interestRate)) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(STR(c.negotiatedRate)) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(STR(c.previousRate)) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(STR(c.amountFrom)) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(STR(c.amountTo)) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(FUNCTION('TO_CHAR', c.effectiveDate, 'DD/MM/YYYY')) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(FUNCTION('TO_CHAR', c.docEffectiveDate, 'DD/MM/YYYY')) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(c.docNo) LIKE CONCAT('%', LOWER(:keyword), '%')) " +
            "ORDER BY c.effectiveDate ASC")
    Page<CtgCfgInterestRateDtlDto> getDetailInterestRateDtls(@Param("interestCode") String interestCode,
                                                             @Param("keyword") String keyword,
                                                             Pageable pageable);

    @Query(value = "SELECT new com.naas.category_service.dto.CtgCfgInterestRateDtl.CtgCfgInterestRateDtlDto(" +
            "c.id," +
            "c.interestRate, c.negotiatedRate, " +
            "c.previousRate, c.effectiveDate, " +
            "c.amountFrom, c.amountTo, " +
            "c.docNo, c.docEffectiveDate) " +
            "FROM CtgCfgInterestRateDtl c " +
            "WHERE c.interestCode = :interestCode " +
            "AND (:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(STR(c.interestRate)) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(STR(c.negotiatedRate)) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(STR(c.previousRate)) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(STR(c.amountFrom)) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(STR(c.amountTo)) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(FUNCTION('TO_CHAR', c.effectiveDate, 'DD/MM/YYYY')) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(FUNCTION('TO_CHAR', c.docEffectiveDate, 'DD/MM/YYYY')) LIKE CONCAT('%', LOWER(:keyword), '%') OR " +
            "LOWER(c.docNo) LIKE CONCAT('%', LOWER(:keyword), '%')) " +
            "ORDER BY c.effectiveDate ASC ")
    List<CtgCfgInterestRateDtlDto> getAll(@Param("interestCode") String interestCode,
                                          @Param("keyword") String keyword);
}
