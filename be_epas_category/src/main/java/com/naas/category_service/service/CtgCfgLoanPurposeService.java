package com.naas.category_service.service;

import com.naas.category_service.dto.CtgCfgLoanPurpose.CtgCfgLoanPurposeDto;
import com.naas.category_service.dto.CtgCfgLoanPurpose.CtgCfgLoanPurposeResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgLoanPurposeService {
    CtgCfgLoanPurposeDto findOne(Long id);
    void create(CtgCfgLoanPurposeDto ctgCfgLoanPurposeDto);
    void update(Long id, CtgCfgLoanPurposeDto ctgCfgLoanPurposeDto);
    void delete(Long id);
    Page<CtgCfgLoanPurposeResponse> searchAll(String filter,Pageable pageable, boolean isExport);
    ResponseEntity<ByteArrayResource> exportToExcel(String filter, List<String> labels);
    boolean checkExist(String code);
}
