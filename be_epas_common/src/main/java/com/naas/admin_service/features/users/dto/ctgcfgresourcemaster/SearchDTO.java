package com.naas.admin_service.features.users.dto.ctgcfgresourcemaster;

import com.naas.admin_service.core.excel.dto.request.PageableRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDTO {
    private String filter;
    private PageableRequest pageable;

}
