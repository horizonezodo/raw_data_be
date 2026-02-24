package com.naas.admin_service.features.mail.service;

import com.naas.admin_service.core.excel.dto.request.ExportExcelRequest;
import com.naas.admin_service.core.excel.dto.request.SearchFilterRequest;
import com.naas.admin_service.features.mail.dto.comcfgmailfrom.ComCfgMailFromDto;
import com.naas.admin_service.features.mail.dto.comcfgmailfrom.ComCfgMailFromResponse;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface ComCfgMailFromService {
    void createComCfgMailFrom(ComCfgMailFromDto comCfgMailFromDto);
    void updateComCfgMailFrom(Long id, ComCfgMailFromDto comCfgMailFromDto);
    ComCfgMailFromDto findComCfgMailFromById(Long id);

    Page<ComCfgMailFromResponse> searchConCfgMailFrom(SearchFilterRequest searchFilterRequest);

    void deleteComCfgMailFromById(Long id);

    ResponseEntity<byte[]> exportExcel(String fileName, ExportExcelRequest request);
}
