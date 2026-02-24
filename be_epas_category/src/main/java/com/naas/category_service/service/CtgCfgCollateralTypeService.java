package com.naas.category_service.service;

import com.naas.category_service.dto.CtgCfgCollateralType.CtgCfgCollateralTypeDto;
import com.naas.category_service.dto.CtgCfgCollateralType.CtgCfgCollateralTypeResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgCollateralTypeService {
    CtgCfgCollateralTypeDto findOne(Long id);
    void create(CtgCfgCollateralTypeDto ctgCfgCollateralTypeDto);
    void update(Long id, CtgCfgCollateralTypeDto ctgCfgCollateralTypeDto);
    void delete(Long id);
    Page<CtgCfgCollateralTypeResponse> searchAll(String filter, Pageable pageable, boolean isExport);
    ResponseEntity<ByteArrayResource> exportToExcel(String filter, List<String> labels);
    boolean checkExist(String code);


}
