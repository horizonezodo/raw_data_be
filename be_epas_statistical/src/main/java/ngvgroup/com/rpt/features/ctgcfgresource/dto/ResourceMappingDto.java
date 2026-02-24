package ngvgroup.com.rpt.features.ctgcfgresource.dto;

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
