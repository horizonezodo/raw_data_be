package com.naas.admin_service.features.permission.repository;

import com.naas.admin_service.features.permission.model.ComCfgPermissionMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PermissionRepository extends JpaRepository<ComCfgPermissionMap, Long> {

    @Query("""
        select distinct m.permissionCode
        from ComCfgPermissionMap m
        where m.userId = :userId
    """)
    List<String> findDistinctPermissionCodesByUserId(@Param("userId") String userId);

    @Query("""
        select distinct m.permissionCode
        from ComCfgPermissionMap m
        where m.groupName = :groupName
    """)
    List<String> findDistinctPermissionCodesByGroupName(@Param("groupName") String groupName);
}
