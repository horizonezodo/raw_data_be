package ngvgroup.com.hrm.feature.cfg_org_unit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HrmCfgOrgUnitDTO {
    @NotBlank(message = "Mã phòng ban đơn vị không được để trống")
    @Size(max = 64, message = "Mã phòng ban đơn vị không vượt quá 64 ký tự")
    private String orgUnitCode;

    @NotBlank(message = "Tên phòng ban đơn vị không được để trống")
    @Size(max = 128, message = "Tên phòng ban đơn vị không vượt quá 128 ký tự")
    private String orgUnitName;

    @NotBlank(message = "Mã cha không được để trống")
    @Size(max = 128, message = "Mã cha không vượt quá 128 ký tự")
    private String parentCode;

    @NotBlank(message = "Mã loại đơn vị được để trống")
    @Size(max = 64, message = "Mã loại đơn vị không vượt quá 64 ký tự")
    private String unitTypeCode;

    @NotBlank(message = "Level cơ cấu tổ chức không được để trống")
    @Size(max = 64, message = "Level cơ cấu tổ chức không vượt quá 64 ký tự")
    private String orgLevelCode;

    @Size(max = 512, message = "Mã tổ chức không vượt quá 512 ký tự")
    private String description;

    private Integer isActive;
}
