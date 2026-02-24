package ngvgroup.com.rpt.features.transactionreport.repository;

import ngvgroup.com.rpt.features.transactionreport.dto.sub.AdjustmentInformationDto;
import ngvgroup.com.rpt.features.transactionreport.dto.sub.CheckDetailDto;
import ngvgroup.com.rpt.features.transactionreport.model.RptTxnStatTemplateKpi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface RptTxnStatTemplateKpiRepository extends JpaRepository<RptTxnStatTemplateKpi, Long> {

    @Query("""
                SELECT new ngvgroup.com.rpt.features.transactionreport.dto.sub.CheckDetailDto(
                    k.id,
                    t.statInstanceCode,
                    k.kpiCode,
                    k.kpiName,
                    t.templateCode
                )
                FROM RptTxnStatTemplate t
                    LEFT JOIN CtgCfgStatTemplateKpi tkp ON t.templateCode = tkp.templateCode
                    LEFT JOIN CtgCfgStatKpi k ON k.kpiCode = tkp.kpiCode
                WHERE t.statInstanceCode = :statInstanceCode
                    AND(:search IS NULL OR
                           LOWER(k.kpiCode) LIKE LOWER(CONCAT('%', :search, '%')) OR
                           LOWER(k.kpiName) LIKE LOWER(CONCAT('%', :search, '%')) OR
                           LOWER(t.templateCode) LIKE LOWER(CONCAT('%', :search, '%')))
                    AND t.isActive = 1
                    AND k.isValid = true
            """)
    Page<CheckDetailDto> searchCheckDetail(
            @Param("statInstanceCode") String statInstanceCode,
            @Param("search") String search,
            Pageable pageable
    );


    @Query("""
        SELECT new ngvgroup.com.rpt.features.transactionreport.dto.sub.AdjustmentInformationDto(
            ka.id,
            tk.kpiName,
            ka.kpiOldValue,
            ka.kpiNewValue,
            ka.changedAt
        )
        FROM RptTxnStatKpiAudit ka 
        LEFT JOIN RptTxnStatTemplateKpi tk ON ka.kpiCode = tk.kpiCode AND ka.statInstanceCode = tk.statInstanceCode
        WHERE ka.statInstanceCode = :statInstanceCode 
            AND(:search IS NULL OR
                   LOWER(tk.kpiName) LIKE LOWER(CONCAT('%', :search, '%')))
            AND tk.isActive = 1
    """)
    List<AdjustmentInformationDto> searchAdjustmentInformation(
            @Param("statInstanceCode") String statInstanceCode,
            @Param("search") String search
    );

    @Query("SELECT t FROM RptTxnStatTemplateKpi t " +
           "WHERE t.statInstanceCode = :instanceCode " +
           "AND t.kpiCode IN :kpiCodes " +
           "AND t.isActive = 1") 
    List<RptTxnStatTemplateKpi> findActiveKpis(
            @Param("instanceCode") String instanceCode,
            @Param("kpiCodes") List<String> kpiCodes
    );

}