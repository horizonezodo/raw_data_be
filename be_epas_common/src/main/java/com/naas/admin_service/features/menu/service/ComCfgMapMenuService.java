package com.naas.admin_service.features.menu.service;

import java.util.List;

import com.naas.admin_service.features.menu.dto.com_cfg_menu.ComCfgMenuDTO;
import com.naas.admin_service.features.menu.dto.com_cfg_menu.ComCfgMenuResDto;

public interface ComCfgMapMenuService {
    List<ComCfgMenuResDto> listMenuMapping() ;

    List<ComCfgMenuDTO> listMenuCheckedByUser(String userId) ;

    List<ComCfgMenuDTO> listMenuCheckedByGroup(String groupName) ;

    void grantMenuToUser(List<String> menuIds, String userId) ;

    void grantMenuToGroup(List<String> menuIds, String groupName) ;

}
