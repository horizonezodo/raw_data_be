package com.naas.admin_service.features.menu.dto.com_cfg_menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComCfgMenuDTO {
    private Long id;
    private String parentId;
    private String menuId;
    private String menuName;
    private Boolean isChecked;
    private List<ComCfgMenuDTO> subMenu;

    public List<ComCfgMenuDTO> getSubMenu() {
        if (subMenu == null) {
            subMenu = new ArrayList<>();
        }
        return subMenu;
    }

}
