package ngvgroup.com.rpt.features.ctgcfgstatruletype.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatRuleTypeDto {

    // ====== SYSTEM INFO ======
    private Long id; // Mã định danh (tự sinh - không nhập từ FE)

    @NotBlank(message = "Trạng thái bản ghi không được để trống")
    @Pattern(regexp = "wait|submit|reject|cancel|approval", message = "Trạng thái bản ghi không hợp lệ (wait, submit, reject, cancel, approval)")
    private String recordStatus; // Trạng thái kiểm soát duyệt

    @NotBlank(message = "Mã tổ chức không được để trống")
    @Size(max = 64, message = "Mã tổ chức tối đa 64 ký tự")
    private String orgCode; // Mã tổ chức

    @NotBlank(message = "Mã loại quy tắc không được để trống")
    @Size(max = 64, message = "Mã loại quy tắc tối đa 64 ký tự")
    private String ruleTypeCode; // Mã loại quy tắc

    @NotBlank(message = "Tên loại quy tắc không được để trống")
    @Size(max = 256, message = "Tên loại quy tắc tối đa 256 ký tự")
    private String ruleTypeName; // Tên loại quy tắc

    private String description;
}