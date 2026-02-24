package com.naas.admin_service.features.menu.controller;

import com.naas.admin_service.features.menu.dto.com_cfg_menu.ComCfgMenuDTO;
import com.naas.admin_service.features.menu.dto.com_cfg_menu.ComCfgMenuResDto;
import com.naas.admin_service.features.menu.service.ComCfgMapMenuService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/map-menu")
@RequiredArgsConstructor
public class ComCfgMapMenuController {
    private final ComCfgMapMenuService comCfgMapMenuService;

    @GetMapping
    public ResponseEntity<ResponseData<List<ComCfgMenuResDto>>> listMenuMapping() {
        List<ComCfgMenuResDto> menus = comCfgMapMenuService.listMenuMapping();
        return ResponseData.okEntity(menus);
    }


    @GetMapping("/is-checked-user/{userId}")
    public ResponseEntity<ResponseData<List<ComCfgMenuDTO>>> listMenuCheckedByUser(@PathVariable String userId)
            {
        List<ComCfgMenuDTO> menus = comCfgMapMenuService.listMenuCheckedByUser(userId);
        return ResponseData.okEntity(menus);
    }


    @GetMapping("/is-checked-group/{groupName}")
    public ResponseEntity<ResponseData<List<ComCfgMenuDTO>>> listMenuCheckedByGroup(@PathVariable String groupName)
            {
        List<ComCfgMenuDTO> menus = comCfgMapMenuService.listMenuCheckedByGroup(groupName);
        return ResponseData.okEntity(menus);
    }

    @PreAuthorize("hasRole('admin_menu')")
    @PutMapping("/grant-user/{userId}")
    public ResponseEntity<ResponseData<Void>> grantMenuToUser(@RequestBody List<String> menuIds,
            @PathVariable String userId) {
        comCfgMapMenuService.grantMenuToUser(menuIds, userId);
        return ResponseData.okEntity(null);
    }

    @PreAuthorize("hasRole('admin_menu')")
    @PutMapping("/grant-group/{groupName}")
    public ResponseEntity<ResponseData<Void>> grantMenuToGroup(@RequestBody List<String> menuIds,
            @PathVariable String groupName) {
        comCfgMapMenuService.grantMenuToGroup(menuIds, groupName);
        return ResponseData.okEntity(null);
    }

}
