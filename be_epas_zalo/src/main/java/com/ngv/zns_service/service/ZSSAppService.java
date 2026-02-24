package com.ngv.zns_service.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

import com.ngv.zns_service.dto.request.SearchFilterRequest;
import com.ngv.zns_service.dto.request.ZSSAppRequest;
import com.ngv.zns_service.dto.request.ZSSAppSearchRequest;
import org.springframework.data.domain.Page;
import com.ngv.zns_service.dto.response.ZSSAppResponse;
import com.ngv.zns_service.exception.ValidationException;
import com.ngv.zns_service.model.entity.ZssApp;

public interface ZSSAppService {
    Page<ZSSAppResponse> searchAll(SearchFilterRequest filterRequest);

    ZSSAppResponse getZssApp(String appId);

    void createZssApp(ZSSAppRequest zssAppRequest) throws ValidationException;

    void updateZssApp(ZSSAppRequest zssAppRequest) throws ValidationException;

    void deleteZssApp(String appId);

    ZssApp getZssAppByOaId(String oaId);

    ZssApp getByWebhookVerificationFile(String whVerificationFile);

    ZssApp getById(String appId);

    ResponseEntity<ByteArrayResource> exportToExcel(ZSSAppSearchRequest request, String fileName);
}
