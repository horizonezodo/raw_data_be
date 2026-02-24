package com.naas.admin_service.features.menu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.naas.admin_service.features.menu.model.ComCfgMenu;

@Repository
public interface ComCfgMenuRepository extends JpaRepository<ComCfgMenu, Long> {
    Optional<ComCfgMenu> findByMenuId(String menuId);

    List<ComCfgMenu> findAllByMenuIdIn(Iterable<String> menuIds);

    @Query("SELECT m.menuId FROM ComCfgMenu m WHERE m.parentId = :menuId")
    List<String> findAllByParentId(String menuId);

    @Modifying
    @Query("DELETE FROM ComCfgMenu m WHERE m.menuId IN :childMenuIds")
    void deleteByMenuIdIn(List<String> childMenuIds);
}
