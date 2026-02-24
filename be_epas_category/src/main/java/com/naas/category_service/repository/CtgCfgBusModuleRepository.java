package com.naas.category_service.repository;

import com.naas.category_service.dto.CtgCfgBusModule.CtgCfgBusModuleDto;
import com.naas.category_service.model.CtgCfgBusModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgBusModuleRepository extends JpaRepository<CtgCfgBusModule, Long> {

    @Query("SELECT new com.naas.category_service.dto.CtgCfgBusModule.CtgCfgBusModuleDto(" +
            "c.moduleCode,c.moduleName) " +
            "FROM CtgCfgBusModule c " +
            "WHERE c.isActive=true")
    List<CtgCfgBusModuleDto> getCtgCfgBusModules();

}
