package ngvgroup.com.hrm.feature.employee.model.inf;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.hrm.feature.employee.model.base.HrmBaseEmployee;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "HRM_INF_EMPLOYEE")
public class HrmInfEmployee extends HrmBaseEmployee {
    /** Mã định danh người dùng */
    @Column(name = "USER_ID", length = 64)
    private String userId;

    /** Tên đăng nhập của User */
    @Column(name = "USERNAME", length = 256)
    private String username;
}