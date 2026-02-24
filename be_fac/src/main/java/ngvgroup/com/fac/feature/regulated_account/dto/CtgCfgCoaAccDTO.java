package ngvgroup.com.fac.feature.regulated_account.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgCoaAccDTO {
    private Long id;
    private String orgCode;
    private String coaVersionCode;
    private String coaScope;
    private String accCoaCode;
    private String accCoaName;
    private String isInternal;
    private int isLeaf;
    private int isAllowPosting;
    private Timestamp effectiveDate;
    private Timestamp expiryDate;
    private String accStatus;
    private String parentCode;
    private int accLevel;
    private int accSubLevel;
    private String accType;
    private String accNature;
    private String accScope;
    private String description;
    private String parentName;
    private String commonName;
    private String accCoaMap;
}
