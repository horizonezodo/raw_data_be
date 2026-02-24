package ngvgroup.com.hrm.feature.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HrmInfEmployeeSearchRequest {
    private String orgCode;
    private List<String> orgUnitCodes;
    private List<String> employeeTypeCodes;
    private List<String> statusCodes;
    private String keyword;
}
