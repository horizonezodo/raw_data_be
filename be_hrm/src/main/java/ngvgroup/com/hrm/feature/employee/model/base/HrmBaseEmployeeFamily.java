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
public class HrmBaseEmployeeFamily extends BaseEntity {

    /** Mã tổ chức */
    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;

    /** Mã nhân viên */
    @Column(name = "EMPLOYEE_CODE", length = 128, nullable = false)
    private String employeeCode;

    /** Mã quan hệ */
    @Column(name = "RELATION_CODE", length = 64)
    private String relationCode;

    /** Họ và tên */
    @Column(name = "FULL_NAME", length = 256)
    private String fullName;

    /** Mã giới tính (M/F/O) */
    @Column(name = "GENDER_CODE", length = 16)
    private String genderCode;

    /** Ngày sinh / Năm thành lập */
    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;

    /** Trạng thái đã mất */
    @Column(name = "IS_DIE")
    private Integer isDie;

    /** Địa chỉ */
    @Column(name = "ADDRESS", length = 512)
    private String address;

    /** Nơi công tác */
    @Column(name = "WORKPLACE")
    private String workplace;

    /** Địa chỉ công tác */
    @Column(name = "WORK_ADDRESS")
    private String workAddress;

    /** Chức vụ nơi công tác */
    @Column(name = "JOB_TITLE")
    private String jobTitle;

    /** Thông tin Mail */
    @Column(name = "EMAIL")
    private String email;

    /** Số điện thoại di động */
    @Column(name = "MOBILE_NUMBER", length = 128)
    private String mobileNumber;

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

    /** Trạng thái phụ thuộc */
    @Column(name = "IS_DEPENDENT")
    private Integer isDependent;

    /** Ngày hiệu lực */
    @Column(name = "EFFECTIVE_DATE")
    private LocalDate effectiveDate;

    /** Ngày kết thúc */
    @Column(name = "END_DATE")
    private LocalDate endDate;

}