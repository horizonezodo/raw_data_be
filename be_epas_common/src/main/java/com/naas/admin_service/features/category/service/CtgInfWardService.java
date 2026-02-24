package com.naas.admin_service.features.category.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.naas.admin_service.features.category.dto.CtgInfWardDto;

import java.util.List;

public interface CtgInfWardService {

    Page<CtgInfWardDto> getWards(Pageable pageable);

    Page<CtgInfWardDto> searchWards(String filter,Pageable pageable);

    ResponseEntity<ByteArrayResource> exportToExcel(String filter, String fileName, List<String> labels);

    void create(CtgInfWardDto ctgInfWardDto);

    void update(CtgInfWardDto ctgInfWardDto);

    void delete(String warCode);

    CtgInfWardDto getDetail(String wardCode);

    boolean checkExist(String code);

    List<CtgInfWardDto> getAll();
}
