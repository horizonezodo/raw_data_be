package ngvgroup.com.crm.features.customer.dto;

import lombok.Data;
import java.util.List;

@Data
public class    CustomerSearchRequestDto {
    private String keyword;
    private String orgCode;
    private List<String> areaCodes;
    private List<String> customerTypes;
}