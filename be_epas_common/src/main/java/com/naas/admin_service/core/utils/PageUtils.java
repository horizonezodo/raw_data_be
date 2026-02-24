package com.naas.admin_service.core.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.naas.admin_service.core.excel.dto.request.PageableRequest;

import java.util.Optional;

public class PageUtils {
    public static Pageable toPageable(PageableRequest request) {
        PageableRequest pageableRequest = Optional.ofNullable(request)
                .orElse(new PageableRequest());
        return PageRequest.of(
                pageableRequest.getPage(),
                pageableRequest.getSize(),
                Sort.by(Sort.Direction.fromString(pageableRequest.getSortDirection()),
                        pageableRequest.getSortField())
        );
    }
    private PageUtils(){}
}
