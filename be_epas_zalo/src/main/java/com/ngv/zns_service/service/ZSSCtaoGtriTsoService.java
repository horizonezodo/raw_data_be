package com.ngv.zns_service.service;

import com.ngv.zns_service.dto.request.SearchFilterRequest;
import com.ngv.zns_service.dto.request.ZSSCtaoGtriTsoRequest;
import com.ngv.zns_service.dto.request.ZSSCtaoGtriTsoSearchRequest;
import org.springframework.data.domain.Page;
import com.ngv.zns_service.dto.response.ZSSCtaoGtriTsoResponse;
import com.ngv.zns_service.exception.ValidationException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

public interface ZSSCtaoGtriTsoService {
    ResponseEntity<ByteArrayResource> exportToExcel(ZSSCtaoGtriTsoSearchRequest request, String fileName);
    Page<ZSSCtaoGtriTsoResponse> searchAll(SearchFilterRequest filterRequest);

    void create(ZSSCtaoGtriTsoRequest rq) throws ValidationException;
    void update(String maCtaoGtriTso, ZSSCtaoGtriTsoRequest rq) throws ValidationException;

    void delete(String maCtaoGtriTso) throws ValidationException;
}
