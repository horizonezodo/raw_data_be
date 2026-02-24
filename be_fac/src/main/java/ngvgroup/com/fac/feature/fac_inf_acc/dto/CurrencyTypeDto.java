package ngvgroup.com.fac.feature.fac_inf_acc.dto;

import lombok.Data;

@Data
public class CurrencyTypeDto {
    private String currencyCode;
    private String currencyName;
    private String symbol;
    private String country;
    private Integer decimalPlaces;
}
