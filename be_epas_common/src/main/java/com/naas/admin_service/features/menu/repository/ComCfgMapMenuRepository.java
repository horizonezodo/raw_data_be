package com.naas.admin_service.features.menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.naas.admin_service.features.menu.model.ComCfgMapMenu;

public interface ComCfgMapMenuRepository extends JpaRepository<ComCfgMapMenu, Long> {
    List<ComCfgMapMenu> findAllByUserId(String userId);

    List<ComCfgMapMenu> findAllByGroupNameIn(List<String> groupName);

    void deleteAllByUserId(String userId);

    void deleteAllByGroupName(String groupName);

    void deleteAllByMenuId(String menuId);

    @Modifying
    @Query("DELETE FROM ComCfgMapMenu m WHERE m.menuId IN :childMenuIds")
    void deleteByMenuIdIn(List<String> childMenuIds);
}
