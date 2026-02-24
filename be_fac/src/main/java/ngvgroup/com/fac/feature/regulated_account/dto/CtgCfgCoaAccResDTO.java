package ngvgroup.com.fac.feature.regulated_account.dto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgCoaAccResDTO {
    Long id;
    String accCoaCode;
    String accCoaName;
    String accNature;
    int accLevel;
    String parentCode;
    String coaVersionCode;
    String commonName;
    String accType;
}
