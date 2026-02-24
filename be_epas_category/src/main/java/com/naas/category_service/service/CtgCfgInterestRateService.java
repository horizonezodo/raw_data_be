package com.naas.category_service.service;

import com.naas.category_service.dto.CtgCfgInterestRate.CtgCfgInterestRateDto;
import com.naas.category_service.model.CtgCfgInterestRate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgInterestRateService {

    Page<CtgCfgInterestRateDto> searchAll(String keyword,String orgCode,List<String> moduleCodes, Pageable pageable);

    ResponseEntity<ByteArrayResource> exportToExcel(String keyword, String fileName, List<String> labels);

    void createInterestRate(CtgCfgInterestRateDto ctgCfgInterestRateDto);

    void updateInterestRate(CtgCfgInterestRateDto ctgCfgInterestRateDto);

    void deleteInterestRate(String interestCode);

    CtgCfgInterestRateDto getDetailInterestRate(String interestCode);

    boolean checkExist(String code);
}
