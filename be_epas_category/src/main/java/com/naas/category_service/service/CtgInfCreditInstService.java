package com.naas.category_service.service;

import com.naas.category_service.dto.CtgInfCreditInst.CtgInfCreditInstDTO;
import com.naas.category_service.dto.ExportExcelDTO;
import com.naas.category_service.model.CtgInfCreditInst;
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
