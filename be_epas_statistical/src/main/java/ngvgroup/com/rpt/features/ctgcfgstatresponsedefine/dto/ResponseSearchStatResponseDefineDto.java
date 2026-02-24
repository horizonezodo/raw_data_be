package ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.dto;


import lombok.Getter;
import lombok.Setter;
import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import ngvgroup.com.rpt.core.constant.ExcelColumns;

@Getter
@Setter
public class ResponseSearchStatResponseDefineDto {
    private Long id;
    @ExcelColumn(ExcelColumns.RSD_RESPONSE_CODE)
    private String responseCode;
    @ExcelColumn(ExcelColumns.RSD_RESPONSE_NAME)
    private String responseName;
    @ExcelColumn(ExcelColumns.RSD_DESCRIPTION)
    private String description;

    public ResponseSearchStatResponseDefineDto(Long id, String responseCode, String responseName, String description) {
        this.id = id;
        this.responseCode = responseCode;
        this.responseName = responseName;
        this.description = description;
    }
}
