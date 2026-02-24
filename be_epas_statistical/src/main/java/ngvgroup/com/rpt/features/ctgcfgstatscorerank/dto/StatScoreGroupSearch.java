package ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatScoreGroupSearch {
    private String keyword;
    private int pageNumber;
    private int pageSize;
    private String sortBy;
    private String sortDirection;
}
