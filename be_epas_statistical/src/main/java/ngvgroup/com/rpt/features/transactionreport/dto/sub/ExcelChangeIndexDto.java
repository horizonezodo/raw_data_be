package ngvgroup.com.rpt.features.transactionreport.dto.sub;

import lombok.Data;

@Data
public class ExcelChangeIndexDto {
    private int row;
    private int col;
    private String data;
}
