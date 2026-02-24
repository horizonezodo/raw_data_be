package com.naas.admin_service.features.menu.dto.com_cfg_menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ComCfgMenuReqDto extends ComCfgMenuBaseReqDto {
    private Long id;

    @NotBlank(message = "menuId không được để trống")
    @Size(max = 128, message = "menuId không được vượt quá 128 ký tự")
    @Pattern(regexp = "^[A-Za-z0-9._]+$", message = "menuId chỉ được chứa chữ cái, số, dấu chấm và dấu gạch dưới")
    private String menuId;
}
