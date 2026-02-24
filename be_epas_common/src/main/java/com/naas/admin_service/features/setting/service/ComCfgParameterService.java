package com.naas.admin_service.features.setting.service;

import org.springframework.http.ResponseEntity;

import com.naas.admin_service.features.setting.dto.ComCfgParameterDto;
import com.naas.admin_service.features.setting.dto.ComCfgParameterResponse;
import com.naas.admin_service.core.excel.dto.request.ExportExcelRequest;
import com.naas.admin_service.core.excel.dto.request.SearchFilterRequest;

import java.util.List;

public interface ComCfgParameterService {
    void createComCfgParameter(ComCfgParameterDto request);
    void updateComCfgParameter(Long id, ComCfgParameterDto request);
    void deleteComCfgParameter(Long id);
    ComCfgParameterDto findComCfgParameterById(Long id);
    ComCfgParameterDto findComCfgParameterByCode(String paramCode);
    List<ComCfgParameterResponse> listComCfgParameter(SearchFilterRequest request);

    ResponseEntity<byte[]> exportExcel(ExportExcelRequest request);

    List<ComCfgParameterDto> getAllByCodeAndValue(String paramCode, String paramValue);
}
