package ngvgroup.com.hrm.feature.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrmAuthInfoSearchResponse {
    private String employeeCode;
    private String employeeName;
    private String mobileNumber;
    private String identificationId;
    private String orgUnitCode;
    private String orgUnitName;
    private String positionCode;
    private String positionName;
}
