package ngvgroup.com.crm.features.crm_cfg_project_type.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrmCfgProjectTypeTemplateDto {
    private Long id;
    private String templateCode;
    private String orgCode;
    private String projectTypeCode;
    private String fileName;
}
