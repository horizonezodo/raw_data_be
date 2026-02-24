package ngvgroup.com.hrm.feature.employee.dto;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExportHrmInfEmployeeDTO {
    @ExcelColumn("Mã nhân viên")
    private String employeeCode;

    @ExcelColumn("Tên nhân viên")
    private String employeeName;

    @ExcelColumn("Số di động")
    private String mobileNumber;

    @ExcelColumn("Số CCCD")
    private String identificationId;

    @ExcelColumn("Chức vụ")
    private String positionName;

    @ExcelColumn("Phòng ban")
    private String orgUnitName;

    @ExcelColumn("Trạng thái làm việc")
    private String statusName;
}
