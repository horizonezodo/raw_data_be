package ngvgroup.com.rpt.features.smrtxnscore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchFilterDTO {
    private String ciCode;
    private String scoreInstanceCode;
    private Date startDate;
    private Date endDate;
    private List<String> statusCodes;
}
