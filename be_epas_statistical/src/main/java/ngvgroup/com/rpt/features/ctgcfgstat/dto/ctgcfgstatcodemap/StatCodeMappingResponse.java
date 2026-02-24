package ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatcodemap;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatCodeMappingResponse {
    private String code;
    private String name;
    private String statRegulatoryCode;
}
