package ngvgroup.com.loan.feature.product_proccess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacCfgAccClassDto {

    private Long id;
    private String accClassCode;
    private String accClassName;

    private String accSideType;

    private String accNature;
    private String description;

}
