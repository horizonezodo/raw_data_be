package ngvgroup.com.fac.feature.fac_inf_acc.dto;

import lombok.Data;

import java.util.List;

@Data
public class AccMapValueDto {
    private Integer isRequired;

    private String mapCode;

    private List<DomainItem> domains;

    @Data
    public static class DomainItem {
        private String businessCode;
    }
}
