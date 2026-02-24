package com.naas.admin_service.features.category.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.naas.admin_service.features.category.dto.TemplateReqDto;
import com.naas.admin_service.features.category.dto.TemplateReqExcelDto;
import com.naas.admin_service.features.category.dto.ReportReqDto;
import com.naas.admin_service.features.category.dto.TemplateResDto;

import java.util.List;

public interface ComCfgTemplateService {
  Page<TemplateResDto> searchListTemplate(String keyword, Pageable pageable);

  TemplateResDto createTemplate(TemplateReqDto dto, MultipartFile file, MultipartFile fileMapping);

  TemplateResDto getDetail(String templateCode);

  byte[] downloadFileTemplate(String templateCode);

  byte[] downloadFileMappingTemplate(String templateCode);

  TemplateResDto getDetailWithFile(String templateCode);

  TemplateResDto updateTemplate(String templateCode, TemplateReqDto dto, MultipartFile file, MultipartFile fileMapping);

  void deleteTemplate(String templateCode);

  void removeFile(String templateCode);
  void removeFileMapping(String templateCode);

  boolean existsByTemplateCode(String templateCode);

  List<TemplateResDto> generateExcelFile(TemplateReqExcelDto req);

  byte[] generateReport(ReportReqDto request);
}
