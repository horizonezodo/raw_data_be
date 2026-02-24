package com.naas.admin_service.features.category.service;

import com.naas.admin_service.features.category.dto.CtgInfCreditInstDTO;
import com.naas.admin_service.features.category.dto.ExportExcelDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface CtgInfCreditInstService {
    Page<CtgInfCreditInstDTO> search(String keyword, Pageable pageable);
    CtgInfCreditInstDTO getDetail(String creditInstCode);
    void updateCreditInst(CtgInfCreditInstDTO dto, String creditInstCode);
    void deleteCredistInst(String creditInstCode);
    void createCreditInst(CtgInfCreditInstDTO dto);
    ResponseEntity<ByteArrayResource> exportExcel(ExportExcelDTO dto);

    boolean checkExist(String code);
}
