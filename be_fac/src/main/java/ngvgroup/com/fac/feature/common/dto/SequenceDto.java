package ngvgroup.com.fac.feature.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SequenceDto {
    private String orgCode;
    private String prefix;
    private String table;
    private String column;
    private String date;
    private Integer numToGen;
    private Integer paddedLength;
    private String separator;
}
