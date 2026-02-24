package com.naas.category_service.service;

import com.naas.category_service.dto.ExportExcelDTO;
import com.naas.category_service.dto.CtgInfPosition.ExportCtgInfPositionDTO;
import com.naas.category_service.dto.CtgInfPosition.CtgInfPositionDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgInfPositionService {
    void createPosition(CtgInfPositionDTO dto);
    void updatePosition(String positionCode, CtgInfPositionDTO dto);
    void deletePosition(String positionCode);
    Page<CtgInfPositionDTO> search(String keyword, Pageable pageable);
    CtgInfPositionDTO getDetail(String positionCode);
    ResponseEntity<ByteArrayResource> exportExcel(ExportExcelDTO dto);
    List<ExportCtgInfPositionDTO> listPosition();

    boolean checkExist(String code);
}
