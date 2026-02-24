package ngvgroup.com.rpt.features.ctgcfgstatrulemapping.dto;


import lombok.Getter;

import java.util.List;

@Getter
public class RuleMappingDto {
    private String templateCode;
    private String kpiCode;
    private List<String> ruleCodes;
}
