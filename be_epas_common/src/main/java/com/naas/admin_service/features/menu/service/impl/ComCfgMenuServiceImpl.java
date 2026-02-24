package com.naas.admin_service.features.menu.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.features.menu.dto.com_cfg_menu.ComCfgMenuBaseReqDto;
import com.naas.admin_service.features.menu.dto.com_cfg_menu.ComCfgMenuReqDto;
import com.naas.admin_service.features.menu.dto.com_cfg_menu.ComCfgMenuResDto;
import com.naas.admin_service.features.menu.mapper.ComCfgMenuMapper;
import com.naas.admin_service.features.menu.model.ComCfgMenu;
import com.naas.admin_service.features.menu.repository.ComCfgMapMenuRepository;
import com.naas.admin_service.features.menu.repository.ComCfgMenuRepository;
import com.naas.admin_service.features.menu.service.ComCfgMenuService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComCfgMenuServiceImpl implements ComCfgMenuService {
    private final ComCfgMenuRepository comCfgMenuRepository;
    private final ComCfgMapMenuRepository comCfgMapMenuRepository;
    private final ComCfgMenuMapper mapper;

    @Override
    public List<ComCfgMenuResDto> listMenu() {
        List<ComCfgMenu> listMenu = comCfgMenuRepository.findAll();
        return listMenu.stream()
                .map(mapper::toResDto)
                .toList();
    }

    @Override
    public void createMenu(ComCfgMenuReqDto comCfgMenuReqDto) {
        boolean menuIdExists = comCfgMenuRepository.findByMenuId(comCfgMenuReqDto.getMenuId()).isPresent();
        if (menuIdExists) {
            throw new BusinessException(CommonErrorCode.MENU_ID_ALREADY_EXISTS, comCfgMenuReqDto.getMenuId());
        }
        ComCfgMenu menu = mapper.toEntity(comCfgMenuReqDto);
        comCfgMenuRepository.save(menu);
    }

    @Override
    public void updateMenu(String menuId, ComCfgMenuBaseReqDto comCfgMenuBaseReqDto){
        ComCfgMenu existingMenu = this.getMenuByMenuId(menuId);
        mapper.updateEntity(comCfgMenuBaseReqDto, existingMenu);
        comCfgMenuRepository.save(existingMenu);
    }

    @Override
    @Transactional
    public void deleteMenu(String menuId){
        List<String> childMenuIds = getChildMenusByMenuId(menuId);
        comCfgMapMenuRepository.deleteByMenuIdIn(childMenuIds);
        comCfgMenuRepository.deleteByMenuIdIn(childMenuIds);
    }

    @Override
    public void saveDragAndDropMenu(List<ComCfgMenuReqDto> listMenuReq){
        List<ComCfgMenu> listMenu = comCfgMenuRepository.findAll();
        Map<String, String> parentMap = new HashMap<>();
        for (ComCfgMenuReqDto menuReq : listMenuReq) {
            parentMap.put(menuReq.getMenuId(), menuReq.getParentId());
        }
        for (ComCfgMenu menu : listMenu) {
            if (parentMap.containsKey(menu.getMenuId())) {
                menu.setParentId(parentMap.get(menu.getMenuId()));
            }
        }
        comCfgMenuRepository.saveAll(listMenu);
    }

    private ComCfgMenu getMenuByMenuId(String menuId){
        return comCfgMenuRepository.findByMenuId(menuId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND,menuId));
    }

    private List<String> getChildMenusByMenuId(String menuId) {
        List<String> childMenuIds = comCfgMenuRepository.findAllByParentId(menuId);
        List<String> result = new ArrayList<>();
        for (String childMenuId : childMenuIds) {
            List<String> subChildMenuIds = getChildMenusByMenuId(childMenuId);
            if (!subChildMenuIds.isEmpty()) result.addAll(subChildMenuIds);
        }
        result.add(menuId);
        return result;
    }
}
