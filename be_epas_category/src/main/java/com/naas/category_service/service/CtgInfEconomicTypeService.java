package com.naas.category_service.service;

import com.naas.category_service.dto.CtgInfEconomicType.CtgInfEconomicTypeDto;
import com.naas.category_service.dto.CtgInfEconomicType.CtgInfEconomicTypeResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgInfEconomicTypeService {
    CtgInfEconomicTypeDto findOne(Long id);
    void create(CtgInfEconomicTypeDto dto);
    void update(Long id, CtgInfEconomicTypeDto dto);
    void delete(Long id);
    Page<CtgInfEconomicTypeResponse> searchAll(String filter, Pageable pageable, boolean isExport);
    ResponseEntity<ByteArrayResource> exportToExcel(String filter, List<String> labels);
    boolean checkExist(String code);
}
