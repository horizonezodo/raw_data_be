package com.ngv.zns_service.dto.request;

import lombok.Data;

@Data
public class SearchFilterRequest {
    private String maDvi;
    private String appId;
    private String tenDvi;
    private String filter;
    private PageableRequest pageable;
}
