package ngvgroup.com.rpt.features.smrtxnscore.repository;

import ngvgroup.com.rpt.features.smrtxnscore.dto.SmrTxnScoreDetailDTO;
import ngvgroup.com.rpt.features.smrtxnscore.dto.SmrTxnScoreExportExcelDTO;
import ngvgroup.com.rpt.features.smrtxnscore.dto.SmrTxnScorePageDTO;
import ngvgroup.com.rpt.features.smrtxnscore.model.SmrTxnScore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SmrTxnScoreRepository extends JpaRepository<SmrTxnScore,Long> {
    SmrTxnScore findByScoreInstanceCode(String scoreInstanceCode);

    @Query("""
        SELECT distinct new ngvgroup.com.rpt.features.smrtxnscore.dto.SmrTxnScorePageDTO(
            s.id,
            s.scoreInstanceCode,
            s.ciId,
            s.txnDate,
            s.makerUserName,
            s.currentStatusName,
            d.ciName,
            d.ciCode,
            s.scorePeriod,
            s.statScoreTypeName,
            s.scoreDate,
            s.isFinal
        )FROM SmrTxnScore s
        LEFT JOIN DimCiD d
        ON s.ciId = d.ciId
        WHERE s.isActive = 1
        AND (
            :keyword IS NULL OR
            LOWER(s.scoreInstanceCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(s.ciId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(s.makerUserName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(s.currentStatusName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(TO_CHAR(s.txnDate, 'DD-MM-YYYY')) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(d.ciName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        AND (
            :startDate IS NULL OR
            s.txnDate >= :startDate
        )
        AND (
            :endDate IS NULL OR
            s.txnDate <= :endDate
        )
        AND (
            :scoreInstanceCode IS NULL OR
            s.scoreInstanceCode =:scoreInstanceCode
        )
        AND (
            :ciCode IS NULL OR
            d.ciCode=:ciCode
        )
        AND (
            :statusCodes IS NULL OR
            s.currentStatusCode IN :statusCodes
        )
        ORDER BY s.modifiedDate desc
    """)
    Page<SmrTxnScorePageDTO> pageScore(@Param("keyword")String keyword,
                                       @Param("startDate")Date startDate,
                                       @Param("endDate")Date endDate,
                                       @Param("ciCode")String ciCode,
                                       @Param("scoreInstanceCode")String scoreInstanceCode,
                                       @Param("statusCodes")List<String> statusCodes,
                                       Pageable pageable);


    @Query("""
        SELECT new ngvgroup.com.rpt.features.smrtxnscore.dto.SmrTxnScoreDetailDTO(
            s.scoreInstanceCode,
            s.txnDate,
            s.makerUserName,
            s.statScoreTypeName,
            d.ciName,
            d.ciCode
        )FROM SmrTxnScore s
        LEFT JOIN DimCiD d
        ON s.ciId = d.ciId
        WHERE s.scoreInstanceCode =:scoreInstanceCode
    """)
    SmrTxnScoreDetailDTO getDetail(@Param("scoreInstanceCode")String scoreInstanceCode);

    SmrTxnScore findByIdAndIsFinal(Long id , boolean finalStatus);

    @Query("""
        SELECT distinct new ngvgroup.com.rpt.features.smrtxnscore.dto.SmrTxnScoreExportExcelDTO(
            s.id,
            s.scoreInstanceCode,
            s.ciId,
            s.txnDate,
            s.makerUserName,
            s.currentStatusName,
            d.ciName,
            d.ciCode,
            s.scorePeriod,
            s.statScoreTypeName,
            s.modifiedDate
        )FROM SmrTxnScore s
        LEFT JOIN DimCiD d
        ON s.ciId = d.ciId
        WHERE s.isActive = 1
        AND (
            :keyword IS NULL OR
            LOWER(s.scoreInstanceCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(s.ciId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(s.makerUserName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(s.currentStatusName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(TO_CHAR(s.txnDate, 'DD-MM-YYYY')) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(d.ciName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        AND (
            :startDate IS NULL OR
            s.txnDate >= :startDate
        )
        AND (
            :endDate IS NULL OR
            s.txnDate <= :endDate
        )
        AND (
            :scoreInstanceCode IS NULL OR
            s.scoreInstanceCode =:scoreInstanceCode
        )
        AND (
            :ciCode IS NULL OR
            d.ciCode=:ciCode
        )
         AND (
            :statusCodes IS NULL OR
            s.currentStatusCode IN :statusCodes
        )
        ORDER BY s.modifiedDate desc
    """)
    List<SmrTxnScoreExportExcelDTO> exportData(@Param("keyword")String keyword,
                                               @Param("startDate")Date startDate,
                                               @Param("endDate")Date endDate,
                                               @Param("ciCode")String ciCode,
                                               @Param("scoreInstanceCode")String scoreInstanceCode,
                                               @Param("statusCodes")List<String> statusCodes);

}
