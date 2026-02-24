package ngvgroup.com.hrm.feature.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrmAuthInfoDto {
    private String authFromEmployeeCode;

    private String authDecisionNo;

    private String authFromEmployeeName;

    private String authFromOrgUnitCode;

    private String authFromOrgUnitName;

    private String authFromPositionCode;

    private String authFromPositionName;

    private String authFromMobileNumber;

    private String authFromIdentificationId;

    private String authContent;

    private LocalDate validFrom;

    private LocalDate validTo;
}