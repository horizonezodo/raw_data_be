package com.ngv.zns_service.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ngv.zns_service.dto.request.MauZNSRequest;
import com.ngv.zns_service.dto.request.SearchFilterRequest;
import com.ngv.zns_service.dto.request.ZSSMauZNZSearchRequest;
import com.ngv.zns_service.dto.request.ZssMauZnsTsoRequest;
import org.springframework.data.domain.Page;
import com.ngv.zns_service.dto.response.MauZNSResponse;
import com.ngv.zns_service.dto.response.ZSSMauZNZResponse;
import com.ngv.zns_service.exception.ValidationException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ZSSMauZNZService {
    Page<ZSSMauZNZResponse> findAll(ZSSMauZNZSearchRequest request);

    Page<ZSSMauZNZResponse> searchAll(SearchFilterRequest filterRequest);

    ResponseEntity<ByteArrayResource> exportToExcel(ZSSMauZNZSearchRequest request, String fileName);

    void createMauZns(MauZNSRequest request);

    void updateMauZns(MauZNSRequest request);

    MauZNSResponse mauZnsDetail(String maMau) throws ValidationException;

    void deleteMauZns(String maMau) throws ValidationException;

    ObjectNode sendApproval(MauZNSRequest request) throws IOException;

    List<MauZNSResponse> syncTemplateZns(String maDvi) throws ValidationException;

    void syncTemplateDetailZns(String oaId, String templateId) throws ValidationException;

    void syncAllTemplateZns(String oaId, int offset, int limit, int status) throws ValidationException;

    void changeTemplateStatus(ObjectNode data) throws ValidationException;

    Map<String, String> listLoaiNdungMau();

    Map<String, String> listTthaiMau();

    Map<String, String> listTenDvi();

    Map<String, String> listMucDichGui();

    Map<String, String> listLoaiButton();

    Map<String, String> listGiaTriTSo();

    void updateMaCtaoGtriTso(ZssMauZnsTsoRequest request);

    String previewMau (String maMau) throws ValidationException;
}
