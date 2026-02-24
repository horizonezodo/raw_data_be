package ngvgroup.com.fac.feature.fac_inf_acc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenerateAccountNoRequest {
    private String orgCode;
    private String domainCode;
    private String accScope;
    private String currencyCode;
}
