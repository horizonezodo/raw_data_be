package com.naas.admin_service.features.category.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.naas.admin_service.features.category.dto.CtgInfIndustryDto;
import com.naas.admin_service.features.category.dto.CtgInfIndustryResponse;

import java.util.List;

public interface CtgInfIndustryService {
    CtgInfIndustryDto findOne(Long id);
    void create(CtgInfIndustryDto dto);
    void update(Long id, CtgInfIndustryDto dto);
    void delete(Long id);
    Page<CtgInfIndustryResponse> searchAll(String filter, Pageable pageable, boolean isExport);
    ResponseEntity<ByteArrayResource> exportToExcel(String filter, List<String> labels);
    boolean checkInfIndustryCodeExist(String code);

    CtgInfIndustryDto getByIndustryCode(String code);
    List<CtgInfIndustryDto> getAll();
}
