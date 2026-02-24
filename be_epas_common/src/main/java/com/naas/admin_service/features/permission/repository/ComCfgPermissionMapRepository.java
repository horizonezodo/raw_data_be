package com.naas.admin_service.features.permission.repository;

import com.naas.admin_service.features.users.dto.RoleResponseDto;
import com.naas.admin_service.features.permission.model.ComCfgPermissionMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComCfgPermissionMapRepository extends JpaRepository<ComCfgPermissionMap, Long> {
    List<ComCfgPermissionMap> findByUserIdAndPermissionCodeIn(String userId, List<String> permissionCodes);
    List<ComCfgPermissionMap> findByGroupNameAndPermissionCodeIn(String groupName, List<String> permissionCodes);

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

    @Query("""
        select new com.naas.admin_service.features.users.dto.RoleResponseDto(p.code, p.name)
        from ComCfgPermissionMap m join ComCfgPermission p on m.permissionCode = p.code
        where m.userId = :userId
    """)
    List<RoleResponseDto> findPermissionInfoByUserId(@Param("userId") String userId);

    @Query("""
        select new com.naas.admin_service.features.users.dto.RoleResponseDto(p.code, p.name)
        from ComCfgPermissionMap m join ComCfgPermission p on m.permissionCode = p.code
        where m.groupName = :groupName
    """)
    List<RoleResponseDto> findPermissionInfoByGroupName(@Param("groupName") String groupName);

    List<ComCfgPermissionMap> findByUserId(String userId);
}
