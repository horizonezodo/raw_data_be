package ngvgroup.com.rpt.features.transactionreport.dto.sub;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DumpTableConfigDto {
    int startRow;
    int endRow;
    int startCol;
    int endCol;
    String table;
    String statInstanceCode;
    List<String> colsToUse;
}
