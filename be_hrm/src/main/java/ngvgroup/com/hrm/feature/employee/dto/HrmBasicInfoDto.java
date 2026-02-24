package ngvgroup.com.hrm.feature.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrmBasicInfoDto {
    /** Mã tổ chức */
    private String orgCode;

    /** Tên nhân viên */
    private String employeeName;

    /** Mã loại nhân viên */
    private String employeeTypeCode;

    /** Mã nhân viên */
    private String employeeCode;

    /** Mã giới tính (M/F/O) */
    private String genderCode;

    /** Ngày sinh / Năm thành lập */
    private LocalDate dateOfBirth;

    /** Số định danh (CCCD;MST ..) */
    private String identificationId;

    /** Ngày cấp */
    private LocalDate issueDate;

    /** Ngày hết hiệu lực */
    private LocalDate expiryDate;

    /** Nơi cấp */
    private String issuePlace;

    /** Thông tin Mail */
    private String email;

    /** Số điện thoại di động */
    private String mobileNumber;

    /** Địa chỉ hiện tại */
    private String currentAddress;

    /** Địa chỉ thường trú */
    private String permanentAddress;

    /** Tình trạng hôn nhân */
    private String maritalStatus;

    /** Ngày vào làm */
    private LocalDate joinDate;

    /** Ngày chính thức */
    private LocalDate confirmDate;

    /** Ngày kết thúc */
    private LocalDate endDate;

    /** Mã lý do kết thúc */
    private String terminateReasonCode;

    /** Mã trạng thái */
    private String statusCode;

    // Các trường từ HrmTxnEmployee

    /** Mã loại quy trình */
    private String processTypeCode;

    /** Mã giao dịch quy trình */
    private String processInstanceCode;

    /** Ngày giao dịch */
    private LocalDate txnDate;
}
