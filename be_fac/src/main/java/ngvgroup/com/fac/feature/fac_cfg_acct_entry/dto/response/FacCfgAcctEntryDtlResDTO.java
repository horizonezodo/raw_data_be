package ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class FacCfgAcctEntryDtlResDTO {
    private Long id;
    private String entryType;
    private String accSideType;
    private String accClassCode;
    private String accClassName;
    private String amtType;
    private String amtParamCode;
    private Integer entrySeqNo;
    private String description;
}
