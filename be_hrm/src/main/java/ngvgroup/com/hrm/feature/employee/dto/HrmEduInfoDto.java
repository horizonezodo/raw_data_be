package ngvgroup.com.hrm.feature.employee.dto;

import lombok.Data;

@Data
public class HrmEduInfoDto {
    /** Trình độ học vấn */
    private String eduLevelCode;

    /** Loại hình đào tạo */
    private String eduTypeCode;

    /** Tên trường học */
    private String schoolName;

    /** Tên khoa */
    private String facultyName;

    /** Mã chuyên ngành */
    private String majorCode;

    /** Tên chuyên ngành */
    private String majorName;

    /** Mô tả chuyên ngành */
    private String majorDesc;

    /** Từ năm */
    private Long fromYear;

    /** Đến năm */
    private Long toYear;

    /** Trạng thái tốt nghiệp */
    private Long isGraduated;

    /** Xếp loại tốt nghiệp */
    private String classificationCode;

    /** Năm tốt nghiệp */
    private Long graduationYear;

    /** Ngày cấp bằng */
    private Long certIssueDate;
}
