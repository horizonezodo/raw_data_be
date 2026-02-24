package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileUploadRequest {
    private String templateFileName;
    private String templateReportFileName;
    private String userGuideFileName;

    private boolean isCreate;
}
