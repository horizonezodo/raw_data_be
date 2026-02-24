package com.naas.admin_service.features.common.repository;

import com.naas.admin_service.features.common.dto.ComCfgBusModuleDto;
import com.naas.admin_service.features.common.model.ComCfgBusModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComCfgBusModuleRepository extends JpaRepository<ComCfgBusModule, Long> {

    @Query("SELECT new com.naas.admin_service.features.common.dto.ComCfgBusModuleDto(" +
            "c.moduleCode,c.moduleName) " +
            "FROM ComCfgBusModule c " +
            "WHERE c.isActive=1")
    List<ComCfgBusModuleDto> getCtgCfgBusModules();

}
