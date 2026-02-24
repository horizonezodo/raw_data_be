package ngvgroup.com.bpm.features.sla.repository;
import ngvgroup.com.bpm.features.sla.dto.CtgCfgProcessTypeDto;
import ngvgroup.com.bpm.features.sla.model.ComCfgProcessType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComCfgProcessTypeRepository extends JpaRepository<ComCfgProcessType, Long> {
    Optional<ComCfgProcessType> findByProcessTypeCode(String processTypeCode);
    @Query("""
        select p.processTypeName
        from ComCfgProcessType p
        where p.processTypeCode = :code
          and p.isDelete = 0
    """)
    String getNameByCode(@Param("code") String code);

    @Query("SELECT new ngvgroup.com.bpm.features.sla.dto.CtgCfgProcessTypeDto(" +
            "c.processTypeCode," +
            "c.processTypeName," +
            "c.isAccounting) " +
            "FROM ComCfgProcessType c")
    List<CtgCfgProcessTypeDto> getAllProcess();
}