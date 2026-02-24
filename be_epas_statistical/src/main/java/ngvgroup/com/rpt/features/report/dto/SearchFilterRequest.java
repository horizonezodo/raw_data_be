package ngvgroup.com.rpt.features.report.dto;

import lombok.Data;

@Data
public class SearchFilterRequest {
    private String filter;
    private PageableDTO pageable;
}

