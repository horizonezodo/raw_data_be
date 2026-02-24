package com.ngv.zns_service.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ngv.zns_service.dto.request.SearchFilterRequest;
import com.ngv.zns_service.dto.request.ZssTKhoanZoaSearchRequest;
import org.springframework.data.domain.Page;
import com.ngv.zns_service.dto.response.ZssTKhoanZoaResponse;
import com.ngv.zns_service.model.entity.ZssTKhoanZoa;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ZssTKhoanZoaService {
    ZssTKhoanZoa setToken(String oaId, String authorizationCode, String state);
    ZssTKhoanZoa getTokenInfo(String maZoa);
    List<ObjectNode> getAll(Integer page, Integer size);
    ZssTKhoanZoa getZoa(String maZoa);
    ZssTKhoanZoa addZoa(ZssTKhoanZoa zssTKhoanZoa);
    ZssTKhoanZoa updateZoa(ZssTKhoanZoa zssTKhoanZoa);
    ZssTKhoanZoa deleteZoa(String maZoa);
    ResponseEntity<ByteArrayResource> exportToExcel(ZssTKhoanZoaSearchRequest request, String fileName);
    Page<ZssTKhoanZoaResponse> searchAll(SearchFilterRequest filterRequest);
    Map<String, String> listTenDviAll();
}
