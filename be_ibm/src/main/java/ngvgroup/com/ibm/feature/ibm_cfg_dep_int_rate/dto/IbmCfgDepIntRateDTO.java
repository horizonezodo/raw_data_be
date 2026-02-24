package ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IbmCfgDepIntRateDTO {
    @NotBlank(message = "Mã lãi suất không được để trống")
    @Size(max = 50, message = "Mã lãi suất không vượt quá 50 ký tự")
    private String interestRateCode;

    @NotBlank(message = "Đơn vị áp dụng không được để trống")
    private String orgCode;

    @NotBlank(message = "Tên lãi suất không được để trống")
    @Size(max = 255, message = "Tên lãi suất không vượt quá 255 ký tự")
    private String interestRateName;

    @NotBlank(message = "Loại lãi suất không được để trống")
    private String interestRateType;

    @NotBlank(message = "Loại tiền tệ không được để trống")
    private String currencyCode;

    @NotNull(message = "Trạng thái không được để trống")
    private Integer isActive;

    @Size(max = 512, message = "Mã tổ chức không vượt quá 512 ký tự")
    private String description;

    @Valid
    List<IbmCfgDepIntRateDtlDtTO> ibmCfgDepIntRateDtls;
}
