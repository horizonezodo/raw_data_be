package ngvgroup.com.crm.features.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRelationDetailDTO {
    private String customerCode;
    private String customerName;
    private String mobileNumber;
    private String identificationId;
    private String address;
}
