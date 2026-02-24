package ngvgroup.com.hrm.feature.employee.model.history;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ngvgroup.com.hrm.feature.employee.model.base.HrmBaseEmployee;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "HRM_INF_EMPLOYEE_H")
public class HrmInfEmployeeH extends HrmBaseEmployee {

    /** Mã giao dịch quy trình */
    @Column(name = "DATA_DATE")
    private LocalDate dataDate;

    /** Mã định danh người dùng */
    @Column(name = "USER_ID", length = 64, nullable = false)
    private String userId;

    /** Tên đăng nhập của User */
    @Column(name = "USERNAME", length = 256, nullable = false)
    private String username;

}