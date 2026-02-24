package com.ngv.zns_service.service;

import org.springframework.data.domain.Page;
import com.ngv.zns_service.dto.request.SearchFilterRequest;
import com.ngv.zns_service.dto.response.ZSSDataMiningResponse;
import com.ngv.zns_service.dto.request.ZSSDataMiningSearchRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.core.io.ByteArrayResource;

public interface ZSSDataMiningService {
    Page<ZSSDataMiningResponse> findAll(ZSSDataMiningSearchRequest request);
    Page<ZSSDataMiningResponse> searchAll(SearchFilterRequest request);
    ResponseEntity<ByteArrayResource> exportToExcel(ZSSDataMiningSearchRequest request, String fileName);
}
