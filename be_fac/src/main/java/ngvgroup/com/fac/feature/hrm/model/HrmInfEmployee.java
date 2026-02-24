package ngvgroup.com.fac.feature.hrm.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "HRM_INF_EMPLOYEE")
public class HrmInfEmployee extends BaseEntity {
    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "EMPLOYEE_NAME", length = 256)
    private String employeeName;

    @Column(name = "EMPLOYEE_TYPE_CODE", length = 64)
    private String employeeTypeCode;

    @Column(name = "USER_ID", length = 64)
    private String userId;

    @Column(name = "USERNAME", length = 256)
    private String username;

    @Column(name = "EMPLOYEE_CODE", length = 128)
    private String employeeCode;

    @Column(name = "GENDER_CODE", length = 16)
    private String genderCode;

    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;

    @Column(name = "IDENTIFICATION_ID", length = 128)
    private String identificationId;

    @Column(name = "ISSUE_DATE")
    private LocalDate issueDate;

    @Column(name = "EXPIRY_DATE")
    private LocalDate expiryDate;

    @Column(name = "ISSUE_PLACE", length = 256)
    private String issuePlace;

    @Column(name = "EMAIL", length = 256)
    private String email;

    @Column(name = "MOBILE_NUMBER", length = 128)
    private String mobileNumber;

    @Column(name = "CURRENT_ADDRESS", length = 512)
    private String currentAddress;

    @Column(name = "PERMANENT_ADDRESS", length = 512)
    private String permanentAddress;

    @Column(name = "MARITAL_STATUS", length = 64)
    private String maritalStatus;

    @Column(name = "JOIN_DATE")
    private LocalDate joinDate;

    @Column(name = "CONFIRM_DATE")
    private LocalDate confirmDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "TERMINATE_REASON_CODE", length = 64)
    private String terminateReasonCode;

    @Column(name = "STATUS_CODE", length = 64)
    private String statusCode;
}
