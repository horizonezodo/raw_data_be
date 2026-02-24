package com.naas.admin_service.features.permission.repository;

import com.naas.admin_service.features.permission.model.ComCfgPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComCfgPermissionRepository extends JpaRepository<ComCfgPermission, Long> {
    Optional<ComCfgPermission> findByCode(String code);

    List<ComCfgPermission> findAllByCodeIn(List<String> permissionCodes);
}
