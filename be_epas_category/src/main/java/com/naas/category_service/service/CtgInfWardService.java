package com.naas.category_service.service;

import com.naas.category_service.dto.CtgInfWard.CtgInfWardDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

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


}
