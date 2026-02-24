package ngvgroup.com.hrm.feature.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HrmInfEmployeeListDTO {
    private String employeeCode;
    private String employeeName;
    private String mobileNumber;
    private String identificationId;
    private String positionName;
    private String orgUnitName;
    private String statusName;
}
