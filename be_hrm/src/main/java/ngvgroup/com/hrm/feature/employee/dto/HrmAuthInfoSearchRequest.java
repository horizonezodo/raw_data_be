package ngvgroup.com.hrm.feature.employee.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrmAuthInfoSearchRequest implements Serializable {
    private String keyword;
}
