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
public class HrmBaseEmployeeAuth extends BaseEntity {

    /** Mã tổ chức */
    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;

    /** Mã người nhận ủy quyền */
    @Column(name = "AUTH_EMPLOYEE_CODE", length = 128, nullable = false)
    private String authEmployeeCode;

    /** Mã người ủy quyền */
    @Column(name = "AUTH_FROM_EMPLOYEE_CODE", length = 128, nullable = false)
    private String authFromEmployeeCode;

    /** Quyết định ủy quyền số */
    @Column(name = "AUTH_DECISION_NO", length = 128, nullable = false)
    private String authDecisionNo;

    /** Mã chức vụ người ủy quyền */
    @Column(name = "AUTH_FROM_POSITION_CODE", length = 32, nullable = false)
    private String authFromPositionCode;

    /** Tên chức vụ người ủy quyền */
    @Column(name = "AUTH_FROM_POSITION_NAME", length = 128, nullable = false)
    private String authFromPositionName;

    /** Nội dung ủy quyền */
    @Column(name = "AUTH_CONTENT", length = 512, nullable = false)
    private String authContent;

    /** Hiệu lực từ ngày */
    @Column(name = "VALID_FROM", nullable = false)
    private LocalDate validFrom;

    /** Ngày kết thúc hiện lực */
    @Column(name = "VALID_TO")
    private LocalDate validTo;

}