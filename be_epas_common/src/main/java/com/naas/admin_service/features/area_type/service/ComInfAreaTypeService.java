package com.naas.admin_service.features.area_type.service;

import com.naas.admin_service.features.area_type.dto.ComInfAreaTypeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ComInfAreaTypeService {

    Page<ComInfAreaTypeDto> getAreaTypes(Pageable pageable);

    Page<ComInfAreaTypeDto> findAreaTypes(String keyword, Pageable pageable);

    ResponseEntity<byte[]> exportToExcel(String keyword, String fileName, List<String> labels);

    void createCtgComAreaType(ComInfAreaTypeDto comInfAreaTypeDto) ;

    void updateCtgComAreaType(ComInfAreaTypeDto comInfAreaTypeDto) ;

    void deleteCtgComAreaType(String areaTypeCode) ;

    ComInfAreaTypeDto getDetail(String areaTypeCode);

    List<ComInfAreaTypeDto> getAll();

    List<ComInfAreaTypeDto> getDistinctAreaTypes();
}
