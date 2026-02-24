package com.naas.admin_service.features.category.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.naas.admin_service.features.category.dto.ExportExcelDTO;
import com.naas.admin_service.features.category.dto.CtgInfTitleDTO;

public interface CtgInfTitleService {
    void createTitle(CtgInfTitleDTO dto);
    void updateTitle(String titleCode, CtgInfTitleDTO dto);
    void deleteTitle(String titleCode);
    Page<CtgInfTitleDTO> search(String keyword, Pageable pageable);
    CtgInfTitleDTO getDetail(String titleCode);
    ResponseEntity<ByteArrayResource> exportExcel(ExportExcelDTO dto);

    boolean checkExist(String code);
}
