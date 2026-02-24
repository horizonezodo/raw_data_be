package com.naas.admin_service.features.mail.service;


import com.naas.admin_service.core.excel.dto.request.ExportExcelRequest;
import com.naas.admin_service.core.excel.dto.request.SearchFilterRequest;
import com.naas.admin_service.core.excel.dto.response.PageResponse;
import com.naas.admin_service.features.mail.dto.comcfgmailtemplate.ComCfgMailTemplateRequest;
import com.naas.admin_service.features.mail.dto.comcfgmailtemplate.ComCfgMailTemplateResponse;

import org.springframework.http.ResponseEntity;

public interface ComCfgMailTemplateService {
    PageResponse<ComCfgMailTemplateResponse> getAll(SearchFilterRequest filterRequest);
    ComCfgMailTemplateResponse getDetail(String mailTemplateCode) ;
    void createComCfgMailTemplate(ComCfgMailTemplateRequest comCfgMailTemplateRequest) ;
    void updateComCfgMail(String mailTemplateCode, ComCfgMailTemplateRequest comCfgMailTemplateRequest) ;
    void deleteComCfgMail(String mailTemplateCode) ;
    ResponseEntity<byte[]> exportExcel(String fileName, ExportExcelRequest request);
}
