package com.naas.admin_service.core.excel.dto.request;

import lombok.Data;

@Data
public class SearchFilterRequest {
    private String filter;
    private PageableRequest pageable;
}
