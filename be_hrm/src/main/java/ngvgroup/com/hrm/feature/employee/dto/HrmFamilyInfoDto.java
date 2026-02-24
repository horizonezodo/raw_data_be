package ngvgroup.com.hrm.feature.employee.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HrmFamilyInfoDto {

    /** Mã quan hệ */
    private String relationCode;

    /** Họ và tên */
    private String fullName;

    /** Mã giới tính (M/F/O) */
    private String genderCode;

    /** Ngày sinh / Năm thành lập */
    private LocalDate dateOfBirth;

    /** Trạng thái đã mất */
    private Integer isDie;

    /** Địa chỉ */
    private String address;

    /** Nơi công tác */
    private String workplace;

    /** Địa chỉ công tác */
    private String workAddress;

    /** Chức vụ nơi công tác */
    private String jobTitle;

    /** Thông tin Mail */
    private String email;

    /** Số điện thoại di động */
    private String mobileNumber;

    /** Số định danh (CCCD;MST ..) */
    private String identificationId;

    /** Ngày cấp */
    private LocalDate issueDate;

    /** Ngày hết hiệu lực */
    private LocalDate expiryDate;

    /** Nơi cấp */
    private String issuePlace;

    /** Trạng thái phụ thuộc */
    private Integer isDependent;

    /** Ngày hiệu lực */
    private LocalDate effectiveDate;

    /** Ngày kết thúc */
    private LocalDate endDate;
}
