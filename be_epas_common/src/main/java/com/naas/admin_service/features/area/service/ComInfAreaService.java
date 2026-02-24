package com.naas.admin_service.features.area.service;

import com.naas.admin_service.features.area.dto.ComInfAreaDto;
import com.naas.admin_service.features.area.dto.ComInfAreaRequestDto;
import com.naas.admin_service.features.area.dto.ComInfAreaResponse;
import com.naas.admin_service.features.area.model.ComInfArea;
import com.naas.admin_service.features.category.dto.CtgInfWardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ComInfAreaService {
    Page<ComInfAreaDto> getCtgComAreas(String keyword, String orgCode, Pageable pageable);

    Page<ComInfAreaResponse> searchAll(ComInfAreaRequestDto filterRequest, Pageable pageable);

    ComInfAreaRequestDto findOne(Long id);

    void create(ComInfAreaRequestDto ctgComAreaRequest);

    void update(Long id, ComInfAreaRequestDto ctgComAreaRequest);

    void delete(Long id);

    ResponseEntity<byte[]> exportToExcel(ComInfAreaRequestDto ctgComAreaRequest, String fileName);

    List<ComInfArea> getCtgComAreasByOrgCodes(List<String> orgCodes);

    String getByAreaCode(String areaCode);

    List<CtgInfWardDto> getListWard(String orgCode);

    List<ComInfAreaDto> getListAreaByWard();

    boolean checkExist(String code);


}
