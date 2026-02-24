package vn.com.amc.qtdl.bi_proxy.repository;

import vn.com.amc.qtdl.bi_proxy.dto.ReportDto;
import vn.com.amc.qtdl.bi_proxy.entity.CtgCfgResourceMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceMappingRepository extends JpaRepository<CtgCfgResourceMapping, Long> {
    @Query("SELECT new vn.com.amc.qtdl.bi_proxy.dto.ReportDto (" +
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
