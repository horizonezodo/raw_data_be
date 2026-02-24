package ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.search;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AcctEntryTreeFilter {
    private String moduleCode;
    private List<String> processTypeCode;
}
