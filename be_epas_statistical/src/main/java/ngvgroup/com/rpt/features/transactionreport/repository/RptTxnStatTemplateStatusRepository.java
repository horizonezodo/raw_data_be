package ngvgroup.com.rpt.features.transactionreport.repository;

import ngvgroup.com.rpt.features.transactionreport.dto.sub.KeepTrackActionsDto;
import ngvgroup.com.rpt.features.transactionreport.model.RptTxnStatTemplateStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RptTxnStatTemplateStatusRepository extends JpaRepository<RptTxnStatTemplateStatus, Long> {

    @Query("""
                SELECT new ngvgroup.com.rpt.features.transactionreport.dto.sub.KeepTrackActionsDto(
                    s.statInstanceCode,
                    s.transitionName,
                    s.statusName,
                    s.slaStatus,
                    s.transitionComment,
                    s.txnUserId,
                    s.txnUserName,
                    s.transitionAt,
                    s.slaDueAt,
                    s.slaActualAt
                )
                FROM RptTxnStatTemplateStatus s
                WHERE s.statInstanceCode = :statInstanceCode
                  AND (
                      :search IS NULL OR
                      LOWER(s.transitionName) LIKE LOWER(CONCAT('%', :search, '%')) OR
                      LOWER(s.statusName) LIKE LOWER(CONCAT('%', :search, '%')) OR
                      LOWER(s.txnUserName) LIKE LOWER(CONCAT('%', :search, '%'))
                  ) ORDER BY s.slaActualAt DESC
            """)
    Page<KeepTrackActionsDto> searchKeepTrackActions(
            @Param("statInstanceCode") String statInstanceCode,
            @Param("search") String search,
            Pageable pageable
    );

    Optional<RptTxnStatTemplateStatus> findTopByStatInstanceCodeOrderByIdDesc(String statInstanceCode);
}
