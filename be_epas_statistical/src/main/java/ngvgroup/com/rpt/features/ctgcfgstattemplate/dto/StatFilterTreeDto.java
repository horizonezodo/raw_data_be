package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatFilterTreeDto {
    private String reportModuleCode;
    private String commonCode;
    private String commonName;
    private List<CtgCfgStatTypeDto> types;
}
