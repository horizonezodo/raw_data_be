package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatekpi;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexKpiRequestDto {
    private String templateCode;
    private List<String> indexs; // row_column
}
