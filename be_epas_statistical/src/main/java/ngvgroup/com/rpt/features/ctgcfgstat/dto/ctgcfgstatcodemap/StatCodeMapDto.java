package ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatcodemap;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatCodeMapDto {
    private String statInternalCode;
    private String statTypeCode;
    private String statRegulatoryCode;
}
