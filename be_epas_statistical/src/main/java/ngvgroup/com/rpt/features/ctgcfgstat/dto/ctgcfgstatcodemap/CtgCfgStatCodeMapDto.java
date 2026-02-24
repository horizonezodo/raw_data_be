package ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatcodemap;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CtgCfgStatCodeMapDto {
    private String statRegulatoryCode;
    private String statTypeCode;
    private String reportModuleCode;
    private List<String> statInternalCode;

}
