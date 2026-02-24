package com.naas.admin_service.core.excel.dto.request;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


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

    public Pageable toPageable() {
        Sort.Direction direction = "asc".equalsIgnoreCase(this.sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, this.sortField != null ? this.sortField : "id");
        return PageRequest.of(this.page, this.size, sort);
    }
}
