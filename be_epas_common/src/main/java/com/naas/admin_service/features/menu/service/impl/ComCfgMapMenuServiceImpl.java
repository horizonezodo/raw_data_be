
package com.naas.admin_service.features.menu.service.impl;

import com.naas.admin_service.features.users.service.GroupService;
import com.naas.admin_service.features.menu.dto.com_cfg_menu.ComCfgMenuDTO;
import com.naas.admin_service.features.menu.dto.com_cfg_menu.ComCfgMenuResDto;
import com.naas.admin_service.features.menu.mapper.ComCfgMenuMapper;
import com.naas.admin_service.features.menu.model.ComCfgMapMenu;
import com.naas.admin_service.features.menu.model.ComCfgMenu;
import com.naas.admin_service.features.menu.repository.ComCfgMapMenuRepository;
import com.naas.admin_service.features.menu.repository.ComCfgMenuRepository;
import com.naas.admin_service.features.menu.service.ComCfgMapMenuService;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ComCfgMapMenuServiceImpl extends BaseStoredProcedureService implements ComCfgMapMenuService {
    private final ComCfgMapMenuRepository comCfgMapMenuRepository;
    private final ComCfgMenuRepository comCfgMenuRepository;
    private final ComCfgMenuMapper comCfgMenuMapper;
    private final AddMenuService addMenuService;
    private final GroupService groupService;

    protected ComCfgMapMenuServiceImpl(ComCfgMapMenuRepository comCfgMapMenuRepository, ComCfgMenuRepository comCfgMenuRepository, ComCfgMenuMapper comCfgMenuMapper, AddMenuService addMenuService, GroupService groupService) {
        super();
        this.comCfgMapMenuRepository = comCfgMapMenuRepository;
        this.comCfgMenuRepository = comCfgMenuRepository;
        this.comCfgMenuMapper = comCfgMenuMapper;
        this.addMenuService = addMenuService;
        this.groupService = groupService;
    }


    @Override
    public List<ComCfgMenuResDto> listMenuMapping(){
        String userId = getCurrentUserId();
        List<String> groups = addMenuService.getGroups();
        List<ComCfgMenu> menuList = getMenu(userId, groups);
        return menuList.stream().map(comCfgMenuMapper::toResDto).toList();
    }

    private List<ComCfgMenuDTO> getRootMenus(List<ComCfgMenu> listComCfgMenu) {

        List<ComCfgMenu> allMenus = comCfgMenuRepository.findAll();
        Map<Long, ComCfgMenuDTO> dtoMap = new HashMap<>();
        for (ComCfgMenu menu : allMenus) {
            ComCfgMenuDTO dto = new ComCfgMenuDTO();
            dto.setId(menu.getId());
            dto.setParentId(menu.getParentId());
            dto.setMenuId(menu.getMenuId());
            dto.setMenuName(menu.getMenuName());
            dto.setIsChecked(listComCfgMenu.stream()
                    .anyMatch(userMenu -> userMenu.getId().equals(menu.getId())));
            dto.setSubMenu(new ArrayList<>());
            dtoMap.put(dto.getId(), dto);
        }

        List<ComCfgMenuDTO> rootMenus = dtoMap.values().stream()
                .filter(dto -> dto.getParentId() == null)
                .toList();

        for (ComCfgMenuDTO parentDto : dtoMap.values()) {
            List<ComCfgMenuDTO> children = dtoMap.values().stream()
                    .filter(child -> parentDto.getMenuId() != null
                            && child.getParentId() != null
                            && child.getParentId().equals(parentDto.getMenuId()))
                    .toList();
            parentDto.getSubMenu().addAll(children);
        }

        return rootMenus;
    }

    @Override
    public List<ComCfgMenuDTO> listMenuCheckedByUser(String userId){
        addMenuService.checkUserIdExist(userId);
        List<ComCfgMenu> listMenuByUser = getListMenuByUser(userId);
        return getRootMenus(listMenuByUser);
    }

    public List<ComCfgMenuDTO> listMenuCheckedByGroup(String groupName){
        addMenuService.checkGroupNameNotFound(groupName);
        List<String> groups = new ArrayList<>();
        groups.add(groupName);
        List<ComCfgMenu> listMenuByGroup = getListMenuByGroup(groups);
        return getRootMenus(listMenuByGroup);
    }

    @Override
    @Transactional
    public void grantMenuToUser(List<String> menuIds, String userId){
        addMenuService.checkUserIdExist(userId);
        addMenuService.checkMenuIdsExistByString(menuIds);
        comCfgMapMenuRepository.deleteAllByUserId(userId);
        addMenuService.addMenuToUserByString(menuIds, userId);
    }

    @Override
    @Transactional
    public void grantMenuToGroup(List<String> menuIds, String groupName){
        addMenuService.checkGroupNameNotFound(groupName);
        addMenuService.checkMenuIdsExistByString(menuIds);
        comCfgMapMenuRepository.deleteAllByGroupName(groupName);
        addMenuService.addMenuToGroupByString(menuIds, groupName);
    }

    private List<ComCfgMenu> getListMenuByUser(String userId) {
        Set<String> userMenuIds = comCfgMapMenuRepository.findAllByUserId(userId).stream()
                .map(ComCfgMapMenu::getMenuId)
                .collect(Collectors.toSet());
        return new ArrayList<>(comCfgMenuRepository.findAllByMenuIdIn(userMenuIds));
    }

    private List<ComCfgMenu> getListMenuByGroup(List<String> groupNames) {
        Set<String> groupMenuIds = comCfgMapMenuRepository.findAllByGroupNameIn(groupNames).stream()
                .map(ComCfgMapMenu::getMenuId)
                .collect(Collectors.toSet());
        return new ArrayList<>(comCfgMenuRepository.findAllByMenuIdIn(groupMenuIds));
    }

    private List<ComCfgMenu> getMenu(String userId, List<String> groupNames) {
        List<GroupRepresentation> groupList = groupService.listGroups();
        addMenuService.checkUserIdExist(userId);
        addMenuService.checkGroupNamesExist(groupList, groupNames);

        Set<String> userMenuIds = comCfgMapMenuRepository.findAllByUserId(userId).stream()
                .map(ComCfgMapMenu::getMenuId)
                .collect(Collectors.toSet());

        Set<String> groupMenuIds = comCfgMapMenuRepository.findAllByGroupNameIn(groupNames).stream()
                .map(ComCfgMapMenu::getMenuId)
                .collect(Collectors.toSet());

        // Lấy hợp của hai tập hợp menuId
        Set<String> allMenuIds = new HashSet<>(userMenuIds);
        allMenuIds.addAll(groupMenuIds);

        // Chỉ truy vấn database một lần để lấy các Menu chung
        return new ArrayList<>(comCfgMenuRepository.findAllByMenuIdIn(allMenuIds));
    }
}
