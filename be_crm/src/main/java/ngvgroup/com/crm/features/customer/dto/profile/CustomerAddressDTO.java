package ngvgroup.com.crm.features.customer.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAddressDTO {
    private String countryCode;
    private String provinceCode;
    private String wardCode;
    private String address;
    private Integer isPrimary; // Đánh dấu địa chỉ chính
}