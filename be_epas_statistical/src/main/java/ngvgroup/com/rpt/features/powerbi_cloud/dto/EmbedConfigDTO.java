package ngvgroup.com.rpt.features.powerbi_cloud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmbedConfigDTO {
    private String embedToken;
    private String embedUrl;
    private String reportId;
}