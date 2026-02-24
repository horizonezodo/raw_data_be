package com.naas.admin_service.features.menu.service.impl;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.features.users.service.GroupService;
import com.naas.admin_service.features.menu.model.ComCfgMapMenu;
import com.naas.admin_service.features.menu.model.ComCfgMenu;
import com.naas.admin_service.features.menu.repository.ComCfgMapMenuRepository;
import com.naas.admin_service.features.menu.repository.ComCfgMenuRepository;
import com.naas.admin_service.features.users.service.UserService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddMenuService {
    private final ComCfgMapMenuRepository comCfgMapMenuRepository;
    private final UserService userService;
    private final ComCfgMenuRepository comCfgMenuRepository;
    private final GroupService groupService;

    @Transactional
    public void addMenuToUserByString(List<String> menuIds, String userId) {
        this.checkUserIdExist(userId);
        this.checkMenuIdsExistByString(menuIds);
        List<ComCfgMapMenu> userMenuList = new ArrayList<>();
        for (String menuId : menuIds) {
            ComCfgMapMenu menuMapping = new ComCfgMapMenu();
            menuMapping.setMenuId(menuId);
            menuMapping.setUserId(userId);
            userMenuList.add(menuMapping);
        }
        comCfgMapMenuRepository.saveAll(userMenuList);
    }

    @Transactional
    public void addMenuToGroupByString(List<String> menuIds, String groupName) {
        this.checkGroupNameNotFound(groupName);
        this.checkMenuIdsExistByString(menuIds);
        List<ComCfgMapMenu> userMenuList = new ArrayList<>();
        for (String menuId : menuIds) {
            ComCfgMapMenu menuMapping = new ComCfgMapMenu();
            menuMapping.setMenuId(menuId);
            menuMapping.setGroupName(groupName);
            userMenuList.add(menuMapping);
        }
        comCfgMapMenuRepository.saveAll(userMenuList);
    }

    public void checkUserIdExist(String userId) {
        try {
            userService.getUser(userId).toRepresentation();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.NOT_FOUND, userId);
        }
    }

    public void checkMenuIdsExistByString(List<String> menuIds) {
        List<ComCfgMenu> menuList = comCfgMenuRepository.findAll();
        Map<String, ComCfgMenu> menuMap = menuList.stream()
                .collect(Collectors.toMap(ComCfgMenu::getMenuId, user -> user));

        List<String> alreadyExitsMenuId = new ArrayList<>();
        menuIds.forEach(menuId -> {
            ComCfgMenu menu = menuMap.get(menuId);
            if (menu == null) {
                alreadyExitsMenuId.add(menuId);
            }
        });
        if (!alreadyExitsMenuId.isEmpty()) {
            throw new BusinessException(CommonErrorCode.EXISTS, "Menu ID không tồn tại: " + String.join(", ", alreadyExitsMenuId));
        }
    }

    public void checkGroupNameNotFound(String groupName) {
        List<GroupRepresentation> groupList = groupService.listGroups();
        boolean groupNameExist = groupList.stream()
                .anyMatch(group -> group.getName().equals(groupName));
        if (!groupNameExist) {
            throw new BusinessException(ErrorCode.NOT_FOUND, groupName);
        }
    }

    public List<String> getGroups() {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getCredentials();
        List<String> groups = jwt.getClaim("groups");
        if (groups != null) {
            return groups.stream()
                    .map(group -> group.startsWith("/") ? group.substring(1) : group)
                    .toList();
        } else {
            return new ArrayList<>();
        }
    }

    public void checkGroupNamesExist(List<GroupRepresentation> groupList, List<String> groupNames) {

        Map<String, GroupRepresentation> groupMap = groupList.stream()
                .collect(Collectors.toMap(GroupRepresentation::getName, group -> group));

        List<String> nameNotExists = new ArrayList<>();
        groupNames.forEach(groupName -> {
            GroupRepresentation group = groupMap.get(groupName);
            if (group == null) {
                nameNotExists.add(groupName);
            }
        });
        if (!nameNotExists.isEmpty()) {
            throw new BusinessException(CommonErrorCode.EXISTS,   "Menu ID không tồn tại: " + String.join(", ", nameNotExists));
        }
    }
}
