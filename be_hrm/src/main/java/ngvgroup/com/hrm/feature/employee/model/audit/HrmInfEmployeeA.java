package ngvgroup.com.hrm.feature.employee.model.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ngvgroup.com.hrm.feature.employee.model.base.HrmBaseEmployee;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "HRM_INF_EMPLOYEE_A")
public class HrmInfEmployeeA extends HrmBaseEmployee {

    /** Mã giao dịch quy trình */
    @Column(name = "DATA_TIME")
    private LocalDateTime dataTime;

    /** Mã định danh người dùng */
    @Column(name = "USER_ID", length = 64)
    private String userId;

    /** Tên đăng nhập của User */
    @Column(name = "USERNAME", length = 256)
    private String username;

}