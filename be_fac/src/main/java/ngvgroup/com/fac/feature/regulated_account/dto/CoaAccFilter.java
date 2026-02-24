package ngvgroup.com.fac.feature.regulated_account.dto;

import lombok.Data;

import java.util.List;

@Data
public class CoaAccFilter {
    private List<String> filterList;
    private String keyword;
    private String versionCode;
    private List<String> accTypes;
    private  String isInternal;
}
