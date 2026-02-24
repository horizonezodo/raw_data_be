package com.naas.admin_service.features.menu.dto.com_cfg_menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComCfgMenuResDto {
    private Long id;

    private String menuId;

    private String menuName;

    private String parentId;

    private String menuUrl;

    private Integer sortNumber;

    private String icon;

    private Boolean isChecked;

    private String description;
}
