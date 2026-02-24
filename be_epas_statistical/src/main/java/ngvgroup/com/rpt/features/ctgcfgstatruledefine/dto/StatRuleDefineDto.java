package ngvgroup.com.rpt.features.ctgcfgstatruledefine.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatRuleDefineDto {

    private Long id;

    @NotBlank(message = "Trạng thái bản ghi không được để trống")
    @Size(max = 64, message = "Trạng thái bản ghi không vượt quá 64 ký tự")
    private String recordStatus;

    @NotNull(message = "Trạng thái hoạt động không được để trống")
    @Min(value = 0, message = "Trạng thái hoạt động phải là 0 hoặc 1")
    @Max(value = 1, message = "Trạng thái hoạt động phải là 0 hoặc 1")
    private Integer isActive;

    @NotBlank(message = "Mã tổ chức không được để trống")
    @Size(max = 64, message = "Mã tổ chức không vượt quá 64 ký tự")
    private String orgCode;

    @NotBlank(message = "Mã rule không được để trống")
    @Size(max = 64, message = "Mã rule không vượt quá 64 ký tự")
    private String ruleCode;

    @NotBlank(message = "Tên rule không được để trống")
    @Size(max = 256, message = "Tên rule không vượt quá 256 ký tự")
    private String ruleName;

    @NotBlank(message = "Mã loại quy tắc không được để trống")
    @Size(max = 64, message = "Mã loại quy tắc không vượt quá 64 ký tự")
    private String ruleTypeCode;

    @Size(max = 4000, message = "Biểu thức SQL không vượt quá 4000 ký tự")
    private String expressionSql;

    @NotBlank(message = "Mã phản hồi không được để trống")
    @Size(max = 64, message = "Mã phản hồi không vượt quá 64 ký tự")
    private String responseCode;

    @NotBlank(message = "Chế độ rule không được để trống")
    @Pattern(regexp = "[MW]", message = "Chế độ rule phải là M (bắt buộc) hoặc W (cảnh báo)")
    private String ruleMode;

    @NotNull(message = "Ngày hiệu lực không được để trống")
    @FutureOrPresent(message = "Ngày hiệu lực phải là hiện tại hoặc tương lai")
    private Date effectiveDate;

    @Future(message = "Ngày hết hiệu lực phải là tương lai")
    private Date expiryDate;

    private String description;
}