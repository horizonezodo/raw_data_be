package ngvgroup.com.fac.feature.fac_inf_acc.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class FacInfAccRequest {

    @NotBlank(message = "Mã chi nhánh không được để trống")
    private String orgCode;

    @NotBlank(message = "Loại tài khoản không được để trống")
    private String accType;

    private String domainCode;

    @NotBlank(message = "Phạm vi tài khoản không được để trống")
    private String accScope;

    @NotBlank(message = "Loại tiền tệ không được để trống")
    private String currencyCode;

    @NotBlank(message = "Loại đối tượng không được để trống")
    private String objectTypeCode;

    @NotBlank(message = "Trạng thái tài khoản không được để trống")
    private String accStatus;

    @NotBlank(message = "Tên tài khoản không được để trống")
    private String accName;

    @NotNull(message = "Ngày mở tài khoản không được để trống")
    private Date openDate;

    @NotBlank(message = "Mục đích tài khoản không được để trống")
    private String accPurposeCode;

    @NotBlank(message = "Phân loại tài khoản không được để trống")
    private String accClassCode;

    private Integer isPrimaryAccount;

    @NotNull(message = "Số dư không được để trống")
    @DecimalMin(value = "0", message = "Số dư phải lớn hơn hoặc bằng 0")
    private BigDecimal bal;

    private String accNature;

    private String accNo;

}
