package com.naas.admin_service.features.menu.service;

import java.util.List;

import com.naas.admin_service.features.menu.dto.com_cfg_menu.ComCfgMenuBaseReqDto;
import com.naas.admin_service.features.menu.dto.com_cfg_menu.ComCfgMenuReqDto;
import com.naas.admin_service.features.menu.dto.com_cfg_menu.ComCfgMenuResDto;

public interface ComCfgMenuService {
    List<ComCfgMenuResDto> listMenu();

    void createMenu(ComCfgMenuReqDto menu) ;

    void updateMenu(String menuId, ComCfgMenuBaseReqDto comCfgMenuBaseReqDto) ;

    void deleteMenu(String menuId) ;

    void saveDragAndDropMenu(List<ComCfgMenuReqDto> listMenu) ;

}
