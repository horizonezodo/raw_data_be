package ngvgroup.com.crm.features.customer.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerProfileDTO {

    private CustomerGeneralInfoDTO basicInfo;

    private CustomerIndividualInfoDTO identityInfoPersonal;

    private CustomerCorporateInfoDTO identityInfoCompany;

    private CustomerExtensionInfoDTO extendedInfo;

    private List<CustomerAddressDTO> addressInfo;

    private List<CustomerRelationDTO> relations;
}
