package ngvgroup.com.bpmn.dto.SearchDTO;

import lombok.Getter;
import lombok.Setter;
import ngvgroup.com.bpmn.dto.PageDTO.PageableDTO;

@Getter
@Setter
public class SearchDTO {
    private String filter;
    private PageableDTO pageable;
}
