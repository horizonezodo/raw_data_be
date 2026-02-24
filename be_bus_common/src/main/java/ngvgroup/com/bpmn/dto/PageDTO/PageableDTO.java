package ngvgroup.com.bpmn.dto.PageDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageableDTO {
    private int page;
    private int size;
    private String sortField;
    private String sortDirection;

    public PageableDTO(){
        this.page = 0;
        this.size = 10;
        this.sortField= "id";
        this.sortDirection="desc";
    }
}
