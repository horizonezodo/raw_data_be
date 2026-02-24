package com.naas.admin_service.features.category.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.naas.admin_service.features.category.dto.CtgInfProvinceDto;

import java.util.List;

public interface CtgInfProvinceService {

    Page<CtgInfProvinceDto> getProvinces(Pageable pageable);

    Page<CtgInfProvinceDto> findProvinces(String keyword,Pageable pageable);

    ResponseEntity<ByteArrayResource> exportToExcel(String keyword,String fileName, List<String> labels);

    void createProvince(CtgInfProvinceDto ctgInfProvinceDto);

    void updateProvince(CtgInfProvinceDto ctgInfProvinceDto);

    void deleteProvince(String provinceCode);

    CtgInfProvinceDto getDetailProvince(String provinceCode);

    List<CtgInfProvinceDto> getAll();

    boolean checkExist(String code);
}
