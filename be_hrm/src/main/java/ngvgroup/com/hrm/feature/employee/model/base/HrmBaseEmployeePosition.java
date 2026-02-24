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
public class HrmBaseEmployeePosition extends BaseEntity {

    /** Mã tổ chức */
    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;

    /** Mã phòng ban đơn vị */
    @Column(name = "ORG_UNIT_CODE", length = 64, nullable = false)
    private String orgUnitCode;

    /** Mã nhân viên */
    @Column(name = "EMPLOYEE_CODE", length = 128, nullable = false)
    private String employeeCode;

    /** Mã chức vụ */
    @Column(name = "POSITION_CODE", length = 32, nullable = false)
    private String positionCode;

    /** Hiệu lực từ ngày */
    @Column(name = "VALID_FROM", nullable = false)
    private LocalDate validFrom;

    /** Ngày kết thúc hiện lực */
    @Column(name = "VALID_TO")
    private LocalDate validTo;

}