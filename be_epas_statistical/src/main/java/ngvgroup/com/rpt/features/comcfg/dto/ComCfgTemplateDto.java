package ngvgroup.com.rpt.features.comcfg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComCfgTemplateDto {
    private String templateCode;
    private String fileName;
    private String description;
}
