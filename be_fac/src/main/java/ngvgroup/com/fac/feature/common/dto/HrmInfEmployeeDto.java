package ngvgroup.com.fac.feature.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HrmInfEmployeeDto {
    private String employeeTypeCode;
    private String employeeName;
    private String identificationId;
    private String currentAddress;
}
