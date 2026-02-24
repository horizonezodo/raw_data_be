package ngvgroup.com.rpt.features.smrscore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqSmrScoreSearchDto {
    private String ciId;
    private String scoreTypeCode;
    private Date startDate;
    private Date endDate;
    private String scoreInstanceCode;
}
