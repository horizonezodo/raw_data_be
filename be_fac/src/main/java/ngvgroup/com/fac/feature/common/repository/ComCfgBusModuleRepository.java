package ngvgroup.com.fac.feature.common.repository;

import ngvgroup.com.fac.feature.common.dto.BusModuleProcessTypeCodeDto;
import ngvgroup.com.fac.feature.common.dto.ComCfgBusModuleDto;
import ngvgroup.com.fac.feature.common.model.ComCfgBusModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComCfgBusModuleRepository extends JpaRepository<ComCfgBusModule, Long> {

    @Query("""
            SELECT new ngvgroup.com.fac.feature.common.dto.ComCfgBusModuleDto(
            c.moduleCode,c.moduleName)
            FROM ComCfgBusModule c
            WHERE c.isActive=1
            """)
    List<ComCfgBusModuleDto> getCtgCfgBusModules();

    @Query("""
            SELECT new ngvgroup.com.fac.feature.common.dto.BusModuleProcessTypeCodeDto(
            c.moduleCode, c.moduleName, t.processTypeCode, t.processTypeName)
            FROM ComCfgBusModule c
            LEFT JOIN ComCfgProcessType t
            ON t.moduleCode = c.moduleCode
            WHERE c.isActive = 1 AND
            (:processTypeCode IS NULL OR t.processTypeCode = :processTypeCode)
            """)
    List<BusModuleProcessTypeCodeDto> getBusModulesByProcessTypeCode(@Param("processTypeCode") String processTypeCode);

    @Query("""
            SELECT new ngvgroup.com.fac.feature.common.dto.BusModuleProcessTypeCodeDto(
            c.moduleCode, c.moduleName, t.processTypeCode, t.processTypeName)
            FROM ComCfgBusModule c
            LEFT JOIN ComCfgProcessType t
            ON t.moduleCode = c.moduleCode
            WHERE t.processTypeCode IS NOT NULL
                  AND t.isAccounting = 1
            """)
    List<BusModuleProcessTypeCodeDto> getTreeFilter();
}
