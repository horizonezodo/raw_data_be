package ngvgroup.com.fac.feature.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CrmInfCustDto {
    private String customerCode;
    private String customerName;
    private String identificationId;
    private String currentAddress;
}
