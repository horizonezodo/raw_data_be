package ngvgroup.com.rpt.features.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportExcelRequest extends SearchFilterRequest {
    private List<LabelExcelDto> labels;
}
