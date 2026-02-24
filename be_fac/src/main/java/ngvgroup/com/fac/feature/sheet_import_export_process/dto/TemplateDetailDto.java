package ngvgroup.com.fac.feature.sheet_import_export_process.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TemplateDetailDto {
    private int index;
    private String accNo;
    private String accCoaName;
    private BigDecimal lineForeignAmt;
}
