package ngvgroup.com.bpm.features.com_cfg_txn_content.dto;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.bpm.core.contants.ExcelColumns;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComCfgTxnContentDto {

    private Long id;

    private String orgCode;

    @ExcelColumn(value = ExcelColumns.CONTENT_CODE)
    private String contentCode;

    @ExcelColumn(value = ExcelColumns.CONTENT_TEXT)
    private String contentText;

    private String contentName;

    @ExcelColumn(value = ExcelColumns.PROCESS_TYPE_NAME)
    private String moduleName;

    private String moduleCode;

    private Integer length;

    private List<ComCfgTxnContentDtlDto> details;

    public ComCfgTxnContentDto(Long id, String orgCode, String contentCode, String contentText, String contentName, String moduleCode, Integer length) {
        this.id = id;
        this.orgCode = orgCode;
        this.contentCode = contentCode;
        this.contentText = contentText;
        this.contentName = contentName;
        this.moduleCode = moduleCode;
        this.length = length;
    }
}

