package ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatResponseDefineDto {

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
    private String responseCode; // Mã loại quy tắc

    @NotBlank(message = "Tên loại quy tắc không được để trống")
    @Size(max = 256, message = "Tên loại quy tắc tối đa 256 ký tự")
    private String responseName; // Tên loại quy tắc

    private String description;
}