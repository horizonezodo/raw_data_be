package com.naas.category_service.service;

import com.naas.category_service.dto.ExportExcelDTO;
import com.naas.category_service.dto.CtgInfTitle.CtgInfTitleDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CtgInfTitleService {
    void createTitle(CtgInfTitleDTO dto);
    void updateTitle(String TitleCode, CtgInfTitleDTO dto);
    void deleteTitle(String TitleCode);
    Page<CtgInfTitleDTO> search(String keyword, Pageable pageable);
    CtgInfTitleDTO getDetail(String TitleCode);
    ResponseEntity<ByteArrayResource> exportExcel(ExportExcelDTO dto);

    boolean checkExist(String code);
}
