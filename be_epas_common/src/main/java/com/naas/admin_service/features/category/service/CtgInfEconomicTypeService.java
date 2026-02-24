package com.naas.admin_service.features.category.service;

import com.naas.admin_service.features.category.model.CtgInfEconomicType;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.naas.admin_service.features.category.dto.CtgInfEconomicTypeDto;
import com.naas.admin_service.features.category.dto.CtgInfEconomicTypeResponse;

import java.util.List;

public interface CtgInfEconomicTypeService {
    CtgInfEconomicTypeDto findOne(Long id);
    void create(CtgInfEconomicTypeDto dto);
    void update(Long id, CtgInfEconomicTypeDto dto);
    void delete(Long id);
    Page<CtgInfEconomicTypeResponse> searchAll(String filter, Pageable pageable, boolean isExport);
    ResponseEntity<ByteArrayResource> exportToExcel(String filter, List<String> labels);
    boolean checkExist(String code);

    CtgInfEconomicType getEconomicByTypeCode(String typeCode);
    List<CtgInfEconomicTypeResponse> getAll();
}
