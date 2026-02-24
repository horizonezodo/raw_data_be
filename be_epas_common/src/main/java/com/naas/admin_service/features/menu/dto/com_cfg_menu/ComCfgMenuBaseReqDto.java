package com.naas.admin_service.features.menu.dto.com_cfg_menu;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComCfgMenuBaseReqDto {
    @NotBlank(message = "menuName không được để trống")
    @Size(max = 256, message = "menuName không được vượt quá 256 ký tự")
    private String menuName;

    @Size(max = 128, message = "parentId không được vượt quá 128 ký tự")
    private String parentId;

    @NotBlank(message = "menuUrl không được để trống")
    @Size(max = 256, message = "menuUrl không được vượt quá 256 ký tự")
    private String menuUrl;

    @Min(value = 0, message = "sortNumber phải lớn hơn hoặc bằng 0")
    private Integer sortNumber;

    @Size(max = 32, message = "icon không được vượt quá 32 ký tự")
    private String icon;

    private String description;
}
