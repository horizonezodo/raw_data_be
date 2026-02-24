package com.ngv.zns_service.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ngv.zns_service.dto.request.SearchFilterRequest;
import com.ngv.zns_service.dto.request.ZSSNDungSearchRequest;
import org.springframework.data.domain.Page;
import com.ngv.zns_service.dto.response.ZSSNDungResponse;
import com.ngv.zns_service.exception.ValidationException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ZSSNDungService {
    ResponseEntity<ByteArrayResource> exportToExcel(ZSSNDungSearchRequest request, String fileName);

    Page<ZSSNDungResponse> searchAll(SearchFilterRequest filterRequest);

    Page<ZSSNDungResponse> findAll(ZSSNDungSearchRequest request);

    Map<String, String> listTenDvu();

    void userReceivedMessage(ObjectNode data) throws ValidationException;
}
