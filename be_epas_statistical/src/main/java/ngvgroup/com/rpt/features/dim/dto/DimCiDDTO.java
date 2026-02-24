package ngvgroup.com.rpt.features.dim.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimCiDDTO {
    private String ciId;
    private String ciCode;
    private String ciName;
}
