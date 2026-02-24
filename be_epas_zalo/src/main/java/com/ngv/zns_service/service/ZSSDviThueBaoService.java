package com.ngv.zns_service.service;

import com.ngv.zns_service.dto.request.SearchFilterRequest;
import com.ngv.zns_service.dto.request.ZSSDviThueBaoRequest;
import com.ngv.zns_service.dto.request.ZSSDviThueBaoSearchRequest;
import org.springframework.data.domain.Page;
import com.ngv.zns_service.dto.response.ZSSDviThueBaoResponse;
import com.ngv.zns_service.exception.ValidationException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

import java.util.Map;


public interface ZSSDviThueBaoService {

    Page<ZSSDviThueBaoResponse> findAll(ZSSDviThueBaoSearchRequest request);
    Map<String, String> listTenGoiDvu();
    Map<String, String> listThai();
    Map<String, String> listTenDvi();
    Map<String, String> listZOAID(String maDvi);
    Map<String, String> listTThaiNvu();
    ResponseEntity<ByteArrayResource> exportToExcel(ZSSDviThueBaoSearchRequest request, String fileName);
    Page<ZSSDviThueBaoResponse> searchAll(SearchFilterRequest filterRequest);

    void create(ZSSDviThueBaoRequest rq) throws ValidationException;
    void update(String maThueBao, ZSSDviThueBaoRequest rq) throws ValidationException;


    void delete(String maThueBao) throws ValidationException;
    void stop(String maThueBao) throws ValidationException;

}
