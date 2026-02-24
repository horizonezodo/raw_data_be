package ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class FacCfgAcctEntryResDTO {
    private Long id;
    private String entryTypeCode;
    private String entryName;
    private String description;
    private Integer entrySeqNo;
    private String entryCode;
    private String ledgerType;
    private String entryDir;
}
