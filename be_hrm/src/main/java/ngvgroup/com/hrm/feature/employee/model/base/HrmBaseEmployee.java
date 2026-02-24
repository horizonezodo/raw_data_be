package ngvgroup.com.hrm.feature.employee.model.base;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@MappedSuperclass
public class HrmBaseEmployee extends BaseEntity {

    /** Mã tổ chức */
    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;

    /** Tên nhân viên */
    @Column(name = "EMPLOYEE_NAME", length = 256, nullable = false)
    private String employeeName;

    /** Mã loại nhân viên */
    @Column(name = "EMPLOYEE_TYPE_CODE", length = 64, nullable = false)
    private String employeeTypeCode;

    /** Mã khách hàng */
    @Column(name = "EMPLOYEE_CODE", length = 128, nullable = false)
    private String employeeCode;

    /** Mã giới tính (M/F/O) */
    @Column(name = "GENDER_CODE", length = 16)
    private String genderCode;

    /** Ngày sinh / Năm thành lập */
    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;

    /** Số định danh (CCCD;MST ..) */
    @Column(name = "IDENTIFICATION_ID", length = 128)
    private String identificationId;

    /** Ngày cấp */
    @Column(name = "ISSUE_DATE")
    private LocalDate issueDate;

    /** Ngày hết hiệu lực */
    @Column(name = "EXPIRY_DATE")
    private LocalDate expiryDate;

    /** Nơi cấp */
    @Column(name = "ISSUE_PLACE", length = 256)
    private String issuePlace;

    /** Thông tin Mail */
    @Column(name = "EMAIL", length = 256)
    private String email;

    /** Số điện thoại di động */
    @Column(name = "MOBILE_NUMBER", length = 128)
    private String mobileNumber;

    /** Địa chỉ hiện tại */
    @Column(name = "CURRENT_ADDRESS", length = 512)
    private String currentAddress;

    /** Địa chỉ thường trú */
    @Column(name = "PERMANENT_ADDRESS", length = 512)
    private String permanentAddress;

    /** Tình trạng hôn nhân */
    @Column(name = "MARITAL_STATUS", length = 64)
    private String maritalStatus;

    /** Ngày vào làm */
    @Column(name = "JOIN_DATE")
    private LocalDate joinDate;

    /** Ngày chính thức */
    @Column(name = "CONFIRM_DATE")
    private LocalDate confirmDate;

    /** Ngày kết thúc */
    @Column(name = "END_DATE")
    private LocalDate endDate;

    /** Mã lý do kết thúc */
    @Column(name = "TERMINATE_REASON_CODE", length = 64)
    private String terminateReasonCode;

    /** Mã trạng thái */
    @Column(name = "STATUS_CODE", length = 64)
    private String statusCode;

}