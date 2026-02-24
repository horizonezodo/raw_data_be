package ngvgroup.com.hrm.feature.employee.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HrmPositionInfoDto {

    /** Đơn vị làm việc */
    private String orgCode;

    /** Mã phòng ban đơn vị */
    private String orgUnitCode;

    /** Mã chức vụ */
    private String positionCode;

    /** Hiệu lực từ ngày */
    private LocalDate validFrom;

    /** Ngày kết thúc hiện lực */
    private LocalDate validTo;
}
