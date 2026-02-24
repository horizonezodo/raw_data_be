package ngvgroup.com.rpt.features.smrtxnscore.repository;

import ngvgroup.com.rpt.features.smrtxnscore.dto.SmrTxnScoreStatusDTO;
import ngvgroup.com.rpt.features.smrtxnscore.model.SmrTxnScoreStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmrTxnScoreStatusRepository extends JpaRepository<SmrTxnScoreStatus,Long> {
    @Query("""
        SELECT DISTINCT new ngvgroup.com.rpt.features.smrtxnscore.dto.SmrTxnScoreStatusDTO(
            s.currentStatusCode,
            s.currentStatusName
        )FROM SmrTxnScore s
        WHERE s.isActive = 1
    """)
    List<SmrTxnScoreStatusDTO> getAllData();

    @Query("""
        SELECT DISTINCT new ngvgroup.com.rpt.features.smrtxnscore.dto.SmrTxnScoreStatusDTO(
            s.statScoreTypeCode,
            s.statScoreTypeName
        )FROM CtgCfgStatScoreType s
        WHERE s.isActive = 1
    """)
    List<SmrTxnScoreStatusDTO> getAllRsData();

    @Query("""
        SELECT new ngvgroup.com.rpt.features.smrtxnscore.dto.SmrTxnScoreStatusDTO(
            w.transitionName,
            s.statusCode,
            s.statusName,
            s.transitionComment,
            s.txnUserId,
            s.txnUserName,
            s.transitionAt
        )FROM SmrTxnScoreStatus s
        LEFT JOIN WorkflowTransition w
        ON s.transitionCode = w.transitionCode
        WHERE s.isActive = 1
        AND (
            :keyword IS NULL OR
            LOWER(s.statusCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(s.statusName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(s.transitionComment) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(s.txnUserId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(s.txnUserName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(w.transitionName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(TO_CHAR(s.transitionAt, 'DD-MM-YYYY')) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        AND s.scoreInstanceCode =:scoreInstanceCode
        ORDER BY s.modifiedDate desc
    """)
    Page<SmrTxnScoreStatusDTO> pageData(@Param("keyword")String keyword,@Param("scoreInstanceCode")String scoreInstanceCode, Pageable pageable);
}
