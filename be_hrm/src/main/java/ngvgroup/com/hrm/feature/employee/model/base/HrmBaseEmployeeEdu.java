package ngvgroup.com.hrm.feature.employee.model.base;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class HrmBaseEmployeeEdu extends BaseEntity {

    /** Mã tổ chức */
    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;

    /** Mã nhân viên */
    @Column(name = "EMPLOYEE_CODE", length = 128, nullable = false)
    private String employeeCode;

    /** Trình độ học vấn */
    @Column(name = "EDU_LEVEL_CODE", length = 32)
    private String eduLevelCode;

    /** Loại hình đào tạo */
    @Column(name = "EDU_TYPE_CODE", length = 128)
    private String eduTypeCode;

    /** Tên trường học */
    @Column(name = "SCHOOL_NAME", length = 256)
    private String schoolName;

    /** Tên khoa */
    @Column(name = "FACULTY_NAME", length = 256)
    private String facultyName;

    /** Mã chuyên ngành */
    @Column(name = "MAJOR_CODE", length = 128)
    private String majorCode;

    /** Tên chuyên ngành */
    @Column(name = "MAJOR_NAME", length = 256)
    private String majorName;

    /** Mô tả chuyên ngành */
    @Column(name = "MAJOR_DESC", length = 256)
    private String majorDesc;

    /** Từ năm */
    @Column(name = "FROM_YEAR")
    private Long fromYear;

    /** Đến năm */
    @Column(name = "TO_YEAR")
    private Long toYear;

    /** Trạng thái tốt nghiệp */
    @Column(name = "IS_GRADUATED")
    private Long isGraduated;

    /** Xếp loại tốt nghiệp */
    @Column(name = "CLASSIFICATION_CODE", length = 128)
    private String classificationCode;

    /** Năm tốt nghiệp */
    @Column(name = "GRADUATION_YEAR")
    private Long graduationYear;

    /** Ngày cấp bằng */
    @Column(name = "CERT_ISSUE_DATE")
    private Long certIssueDate;

}