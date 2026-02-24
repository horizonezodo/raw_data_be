package com.naas.admin_service.features.menu.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.naas.admin_service.features.menu.dto.com_cfg_menu.ComCfgMenuBaseReqDto;
import com.naas.admin_service.features.menu.dto.com_cfg_menu.ComCfgMenuReqDto;
import com.naas.admin_service.features.menu.dto.com_cfg_menu.ComCfgMenuResDto;
import com.naas.admin_service.features.menu.service.ComCfgMenuService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class ComCfgMenuController {
    private final ComCfgMenuService comCfgMenuService;

    @Operation(summary = "Danh sách Menu")
    @GetMapping
    public ResponseEntity<ResponseData<List<ComCfgMenuResDto>>> listMenu() {
        List<ComCfgMenuResDto> listMenu = comCfgMenuService.listMenu();
        return ResponseData.okEntity(listMenu);
    }

    @PreAuthorize("hasRole('admin_menu')")
    @PostMapping
    public ResponseEntity<ResponseData<Void>> createMenu(@Valid @RequestBody ComCfgMenuReqDto comCfgMenuReqDto)
            {
        comCfgMenuService.createMenu(comCfgMenuReqDto);
        return ResponseData.createdEntity();
    }

    @PreAuthorize("hasRole('admin_menu')")
    @PutMapping("/{menuId}")
    public ResponseEntity<ResponseData<Void>> updateMenu(@PathVariable String menuId,
            @Valid @RequestBody ComCfgMenuBaseReqDto comCfgMenuBaseReqDto)
            {
        comCfgMenuService.updateMenu(menuId, comCfgMenuBaseReqDto);
        return ResponseData.okEntity(null);
    }

    @PreAuthorize("hasRole ('admin_menu')")
    @DeleteMapping("/{menuId}")
    public ResponseEntity<ResponseData<Void>> deleteMenu(@PathVariable String menuId) {
        comCfgMenuService.deleteMenu(menuId);
        return ResponseData.noContentEntity();
    }

    @PreAuthorize("hasRole('admin_menu')")
    @PutMapping("/save-drag-drop-menu")
    public ResponseEntity<ResponseData<Void>> saveDragAndDropMenu(@RequestBody List<ComCfgMenuReqDto> listMenu)
            {
        comCfgMenuService.saveDragAndDropMenu(listMenu);
        return ResponseData.noContentEntity();
    }
}
