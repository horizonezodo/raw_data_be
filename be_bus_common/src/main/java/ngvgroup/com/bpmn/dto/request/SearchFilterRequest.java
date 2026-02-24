package ngvgroup.com.bpmn.dto.request;

import lombok.Data;
import ngvgroup.com.bpmn.dto.PageDTO.PageableDTO;

@Data
public class SearchFilterRequest {
    private String filter;
    private PageableDTO pageable;
}
