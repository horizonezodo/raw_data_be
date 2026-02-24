package vn.com.amc.qtdl.tableau_proxy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.amc.qtdl.tableau_proxy.models.dtos.ReportDto;
import vn.com.amc.qtdl.tableau_proxy.models.dtos.RptReportExploitationDto;
import vn.com.amc.qtdl.tableau_proxy.models.entities.RptReport;

import java.util.List;

@Repository
public interface RptReportRepository extends JpaRepository<RptReport, Long> {
    @Query("SELECT new vn.com.amc.qtdl.tableau_proxy.models.dtos.ReportDto (" +
            "r.id, r.reportCode,r.reportSource) "
            +
            "FROM CtgCfgResourceMapping rm " +
            "INNER JOIN CtgCfgReport r ON r.reportCode = rm.resourceCode AND rm.resourceTypeCode = 'CM032.003' "
            +
            "AND r.reportType = 'CM036.001' " +
            "WHERE rm.userId = :userId " +
            "ORDER BY r.sortNumber ASC")
    List<ReportDto> findAllReport(@Param("userId") String userId);
}
