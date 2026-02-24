package ngvgroup.com.rpt.features.dim.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimCiBrDDTO {
    private String ciBrCode;
    private String ciBrName;
    private BigDecimal achievedScore;
    private String rankValue;
}
