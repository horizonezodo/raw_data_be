package ngvgroup.com.bpm.features.ctg_cfg_resource.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceMappingDto {
    private Long id;
    private String resourceCode;
    private String resourceName;
}
