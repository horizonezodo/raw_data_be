package ngvgroup.com.bpmn.dto.request;

import ngvgroup.com.bpmn.dto.LabelExcelDto;
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
