package ngvgroup.com.rpt.features.ctgcfgstatus.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.rpt.features.ctgcfgstatus.model.CtgCfgStatusSla;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface CtgCfgStatusSlaRepository extends BaseRepository<CtgCfgStatusSla> {

    // Lấy bản SLA hiện hành theo STATUS_CODE, đang enable + active + hiệu lực theo ngày
    @Query("""
        select s from CtgCfgStatusSla s
        where s.statusCode = :statusCode
          and coalesce(s.isEnable,1) = 1
          and s.isActive = 1
          and s.isDelete = 0
          and (s.effectiveDate is null or s.effectiveDate <= :at)
          and (s.expiryDate is null or s.expiryDate >= :at)
        order by s.effectiveDate desc nulls last
    """)
    Optional<CtgCfgStatusSla> findCurrentByStatusCode(String statusCode, Date at);

    @Query("SELECT c.id from CtgCfgStatusSla c where c.statusCode =:statusCode")
    Long findIdByStatusCode(@Param("statusCode") String statusCode);

    Optional<CtgCfgStatusSla> findByStatusCode(String statusCode);
}
