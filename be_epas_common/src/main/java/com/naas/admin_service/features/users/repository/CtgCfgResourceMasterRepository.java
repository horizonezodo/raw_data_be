package com.naas.admin_service.features.users.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.naas.admin_service.features.users.dto.ctgcfgresourcemaster.CtgCfgResourceMasterDto;
import com.naas.admin_service.features.users.model.CtgCfgResourceMaster;

import java.util.List;

@Repository
public interface CtgCfgResourceMasterRepository extends JpaRepository<CtgCfgResourceMaster, Long> {

    @Query("select new com.naas.admin_service.features.users.dto.ctgcfgresourcemaster.CtgCfgResourceMasterDto( ccrm.id, " +
            "ccrm.resourceTypeCode, ccrm.resourceTypeName, ccrm.resourceSql) " +
            "from CtgCfgResourceMaster ccrm " +
            "where (:filter is null " +
            "or ccrm.resourceTypeCode like concat('%', :filter, '%')" +
            "or ccrm.resourceTypeName like concat('%', :filter, '%') ) ")
    Page<CtgCfgResourceMasterDto> searchComCfgResourceMaster(
            @Param("filter") String filter,
            Pageable pageable);

    @Query("select new com.naas.admin_service.features.users.dto.ctgcfgresourcemaster.CtgCfgResourceMasterDto( ccrm.id, " +
            "ccrm.resourceTypeCode, ccrm.resourceTypeName, null) from CtgCfgResourceMaster ccrm " +
            "where (:filter is null " +
            "or ccrm.resourceTypeCode like concat('%', :filter, '%')" +
            "or ccrm.resourceTypeName like concat('%', :filter, '%') ) ")
    List<CtgCfgResourceMasterDto> searchComCfgResourceMaster(
            @Param("filter") String filter);

    boolean existsByResourceTypeCode(String resourceTypeCode);
}
