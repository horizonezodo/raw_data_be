package com.ngv.zns_service.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageableRequest {
    private int page;
    private int size;
    private String sortField;
    private String sortDirection;

    public PageableRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public PageableRequest() {
        this.page = 0;
        this.size = 10;
        this.sortField = "id";
        this.sortDirection = "desc";
    }


}






