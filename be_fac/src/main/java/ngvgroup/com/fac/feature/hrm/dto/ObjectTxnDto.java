package ngvgroup.com.fac.feature.hrm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectTxnDto {
    private String code;
    private String name;
    private String identificationId;
    private String address;
    private LocalDate issueDate;
    private String issuePlace;
}
