package ngvgroup.com.crm.features.customer.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRelationDTO {
    private String relatedCustomerCode; // Mã khách hàng liên quan
    private String relationGroupCode; // Loại quan hệ (Gia đình, Đối tác...)
    private String relationCode; // Mã quan hệ (Bố, Mẹ, Vợ...)
    private String reciprocalRelationCode; // Mã quan hệ đối ứng
    private String relationStatus; // Trạng thái quan hệ

    private String relatedCustomerName;
    private String mobileNumber;
    private String identificationId;
    private String address;
    private String relationGroupName;
    private String relationName;
    private String reciprocalRelationName;
    private String relationStatusName;
}